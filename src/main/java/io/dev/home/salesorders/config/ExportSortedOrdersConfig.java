package io.dev.home.salesorders.config;

import io.dev.home.salesorders.infrastructure.batch.listener.HeaderCopyCallback;
import io.dev.home.salesorders.infrastructure.persistence.OrdersDb;
import io.dev.home.salesorders.infrastructure.persistence.OrdersFile;
import io.dev.home.salesorders.model.Order;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.BATCH_FILE_OUTPUT;

@Configuration
public class ExportSortedOrdersConfig {

    public static final String EXPORT_SORTED_ORDERS_STEP = "exportSortedOrdersStep";
    public static final int CHUNK_SIZE = 10_000;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step exportSortedOrdersStep(JdbcCursorItemReader<Order> sortedOrdersDatabaseItemReader,
                                       FlatFileItemWriter<Order> sortedOrdersFileItemWriter) {
        return this.stepBuilderFactory.get(EXPORT_SORTED_ORDERS_STEP)
                .<Order, Order>chunk(CHUNK_SIZE)
                .reader(sortedOrdersDatabaseItemReader)
                .processor(new PassThroughItemProcessor<>())
                .writer(sortedOrdersFileItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Order> sortedOrdersDatabaseItemReader(
            DataSource dataSource,
            @Value("#{stepExecution.jobExecution.id}") long jobExecutionId) {
        return new JdbcCursorItemReaderBuilder<Order>()
                .name("sortedOrdersDatabaseItemReader")
                .dataSource(dataSource)
                .sql(OrdersDb.createQuery(jobExecutionId))
                .rowMapper(new BeanPropertyRowMapper<>(Order.class))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Order> sortedOrdersFileItemWriter(
            @Value("#{jobParameters['" + BATCH_FILE_OUTPUT + "']}") String fileOutput,
            HeaderCopyCallback headerCopyCallback) {
        return new FlatFileItemWriterBuilder<Order>()
                .name("sortedOrdersFileItemWriter")
                .resource(new FileSystemResource(fileOutput))
                .delimited()
                .names(OrdersFile.HEADER_WRITE_MAPPING)
                .headerCallback(headerCopyCallback)
                .build();
    }
}
