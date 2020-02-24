package io.dev.home.salesorders.config;

import io.dev.home.salesorders.infrastructure.batch.listener.ChunkLogListener;
import io.dev.home.salesorders.infrastructure.batch.listener.HeaderCopyCallback;
import io.dev.home.salesorders.infrastructure.batch.listener.OrderSkipListener;
import io.dev.home.salesorders.infrastructure.batch.reader.OrderLineMapper;
import io.dev.home.salesorders.infrastructure.persistence.ImportErrorFile;
import io.dev.home.salesorders.infrastructure.persistence.OrdersDb;
import io.dev.home.salesorders.infrastructure.persistence.OrdersFile;
import io.dev.home.salesorders.model.ImportError;
import io.dev.home.salesorders.model.Order;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.policy.NeverRetryPolicy;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;

import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.BATCH_FILE_ERROR;
import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.BATCH_FILE_INPUT;


@Configuration
public class ImportOrdersConfig {

    public static final String IMPORT_ORDERS_STEP = "importOrdersStep";
    public static final int CHUNK_SIZE = 10_000;


    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private static void writeHeader(Writer writer) throws IOException {
        writer.write(ImportErrorFile.HEADER_MAPPING_LINE);
    }

    @Bean
    public Step importOrdersStep(FlatFileItemReader<Order> ordersFileItemReader,
                                 BeanValidatingItemProcessor<Order> orderItemValidator,
                                 JdbcBatchItemWriter<Order> ordersDatabaseItemWriter,
                                 FlatFileItemWriter<ImportError> importErrorItemWriter,
                                 OrderSkipListener orderSkipListener,
                                 ChunkLogListener chunkLogListener) {
        return this.stepBuilderFactory.get(IMPORT_ORDERS_STEP)
                .<Order, Order>chunk(CHUNK_SIZE)
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                //.skip(DuplicateKeyException.class)
                .retryPolicy(new NeverRetryPolicy())
                .reader(ordersFileItemReader)
                .processor(orderItemValidator)
                .writer(ordersDatabaseItemWriter)
                .stream(importErrorItemWriter)
                .listener(orderSkipListener)
                .listener(chunkLogListener)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Order> ordersFileItemReader(
            @Value("#{jobParameters['" + BATCH_FILE_INPUT + "']}") String fileInput,
            HeaderCopyCallback headerCopyCallback,
            OrderLineMapper orderLineMapper) {
        return new FlatFileItemReaderBuilder<Order>()
                .name("ordersFileItemReader")
                .resource(new FileSystemResource(fileInput))
                .linesToSkip(OrdersFile.HEADER_LINES)
                .skippedLinesCallback(headerCopyCallback)
                .lineMapper(orderLineMapper)
                .build();
    }

    @Bean
    @StepScope
    public BeanValidatingItemProcessor<Order> orderItemValidator() throws Exception {
        BeanValidatingItemProcessor<Order> validator = new BeanValidatingItemProcessor<>();
        validator.setFilter(true);
        validator.afterPropertiesSet();
        return validator;
    }

    @Bean
    @StepScope
    public OrderLineMapper orderLineMapper(@Value("#{stepExecution.jobExecution.id}") long jobExecutionId) {
        return new OrderLineMapper(jobExecutionId);
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Order> ordersDatabaseItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Order>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(OrdersDb.createCommandInsert())
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<ImportError> importErrorItemWriter(
            @Value("#{jobParameters['" + BATCH_FILE_ERROR + "']}") String fileError) {
        return new FlatFileItemWriterBuilder<ImportError>()
                .name("importErrorItemWriter")
                .resource(new FileSystemResource(fileError))
                .delimited()
                .names(ImportErrorFile.HEADER_MAPPING)
                .headerCallback(writer -> writer.write(ImportErrorFile.HEADER_MAPPING_LINE))
                .build();
    }

    @Bean
    @StepScope
    public OrderSkipListener orderSkipListener(FlatFileItemWriter<ImportError> importErrorItemWriter) {
        return new OrderSkipListener(importErrorItemWriter);
    }

    @Bean
    @StepScope
    public ChunkLogListener chunkLogListener() {
        return new ChunkLogListener();
    }
}
