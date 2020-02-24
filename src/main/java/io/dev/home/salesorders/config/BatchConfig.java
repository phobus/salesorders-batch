package io.dev.home.salesorders.config;

import io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator;
import io.dev.home.salesorders.infrastructure.batch.listener.HeaderCopyCallback;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@Import({ImportOrdersConfig.class, ExportSortedOrdersConfig.class, ExportSummaryConfig.class})
public class BatchConfig {

    public static final String REGISTER_SALES_ORDERS_JOB = "registerSalesOrdersJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job registerSalesOrdersJob(Step importOrdersStep,
                                      Step exportSortedOrdersStep,
                                      Step exportSummaryStep) {
        return this.jobBuilderFactory.get(REGISTER_SALES_ORDERS_JOB)
                .incrementer(new RunIdIncrementer())
                .validator(fileJobParametersValidator())
                .start(importOrdersStep).on("FAILED").end()
                .from(importOrdersStep).on("COMPLETED ALL SKIPPED").end()
                .from(importOrdersStep).on("*")
                .to(exportSortedOrdersStep)
                .next(exportSummaryStep).end()
                .build();
    }

    @Bean
    public FileJobParametersValidator fileJobParametersValidator() {
        return new FileJobParametersValidator();
    }

    @Bean
    public HeaderCopyCallback headerCopyCallback() {
        return new HeaderCopyCallback();
    }
}
