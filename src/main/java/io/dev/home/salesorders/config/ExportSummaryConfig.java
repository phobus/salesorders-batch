package io.dev.home.salesorders.config;

import io.dev.home.salesorders.infrastructure.batch.writer.SummaryLineAggregator;
import io.dev.home.salesorders.infrastructure.persistence.SummaryDb;
import io.dev.home.salesorders.model.SummaryLine;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.BATCH_FILE_SUMMARY;

@Configuration
public class ExportSummaryConfig {

    public static final String EXPORT_SUMMARY_STEP = "exportSummaryStep";
    public static final int CHUNK_SIZE = 10_000;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step exportSummaryStep(
            JdbcCursorItemReader<SummaryLine> summaryItemReader,
            ItemWriter<SummaryLine> summaryItemWriter) {
        return this.stepBuilderFactory.get(EXPORT_SUMMARY_STEP)
                .<SummaryLine, SummaryLine>chunk(CHUNK_SIZE)
                .reader(summaryItemReader)
                .writer(summaryItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<SummaryLine> summaryItemReader(
            DataSource dataSource,
            @Value("#{stepExecution.jobExecution.id}") long jobExecutionId) {
        return new JdbcCursorItemReaderBuilder<SummaryLine>()
                .name("summaryItemReader")
                .dataSource(dataSource)
                .sql(SummaryDb.createQuery(jobExecutionId))
                .rowMapper(new BeanPropertyRowMapper<>(SummaryLine.class))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<SummaryLine> summaryItemWriter(
            @Value("#{jobParameters['" + BATCH_FILE_SUMMARY + "']}") String fileSummary
    ) {
        return new FlatFileItemWriterBuilder<SummaryLine>()
                .name("summaryItemWriter")
                .resource(new FileSystemResource(fileSummary))
                .lineAggregator(summaryLineAggregator())
                .build();
    }

    @Bean
    public SummaryLineAggregator summaryLineAggregator() {
        return new SummaryLineAggregator();
    }
}
