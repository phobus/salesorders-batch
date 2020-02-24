package io.dev.home.salesorders.endtoend;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import static io.dev.home.salesorders.config.ExportSortedOrdersConfig.EXPORT_SORTED_ORDERS_STEP;
import static io.dev.home.salesorders.config.ImportOrdersConfig.IMPORT_ORDERS_STEP;
import static io.dev.home.salesorders.infrastructure.batch.listener.OrderSkipListener.COMPLETED_WITH_SKIPS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestEndToEndJobImportOrdersTestKit {

    public static final String REGISTRO_VENTAS_1_CSV = "RegistroVentas1.csv";
    public static final String REGISTRO_VENTAS_2_CSV = "RegistroVentas2.csv";

    public static void assertThatJobExecution1IsCorrect(JobExecution jobExecution1) {
        assertThat(jobExecution1.getExitStatus(), equalTo(ExitStatus.COMPLETED));
        for (StepExecution stepExecution : jobExecution1.getStepExecutions()) {
            if (IMPORT_ORDERS_STEP.equals(stepExecution.getStepName())) {
                assertThat(stepExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
                assertThat("TestEndToEnd importOrdersStep error in readCount", stepExecution.getReadCount(), equalTo(1_000));
                assertThat("TestEndToEnd importOrdersStep error in writeCount", stepExecution.getWriteCount(), equalTo(1_000));
                assertThat("TestEndToEnd importOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
                assertThat("TestEndToEnd importOrdersStep error in processSkipCount", stepExecution.getProcessSkipCount(), equalTo(0));
                assertThat("TestEndToEnd importOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
            } else if (EXPORT_SORTED_ORDERS_STEP.equals(stepExecution.getStepName())) {
                assertThat(stepExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readCount", stepExecution.getReadCount(), equalTo(1_000));
                assertThat("TestEndToEnd exportSortedOrdersStep error in writeCount", stepExecution.getWriteCount(), equalTo(1_000));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
                assertThat("TestEndToEnd exportSortedOrdersStep error in processSkipCount", stepExecution.getProcessSkipCount(), equalTo(0));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
            }
        }
    }

    public static void assertThatJobExecution2IsCorrect(JobExecution jobExecution2) {
        assertThat(jobExecution2.getExitStatus(), equalTo(ExitStatus.COMPLETED));
        for (StepExecution stepExecution : jobExecution2.getStepExecutions()) {
            if (IMPORT_ORDERS_STEP.equals(stepExecution.getStepName())) {
                assertThat(stepExecution.getExitStatus().getExitCode(), equalTo(COMPLETED_WITH_SKIPS));
                assertThat("TestEndToEnd importOrdersStep error in readCount", stepExecution.getReadCount(), equalTo(1_000_000));
                assertThat("TestEndToEnd importOrdersStep error in writeCount", stepExecution.getWriteCount(), equalTo(900_000));
                assertThat("TestEndToEnd importOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
                assertThat("TestEndToEnd importOrdersStep error in processSkipCount", stepExecution.getProcessSkipCount(), equalTo(0));
                assertThat("TestEndToEnd importOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
            } else if (EXPORT_SORTED_ORDERS_STEP.equals(stepExecution.getStepName())) {
                assertThat(stepExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readCount", stepExecution.getReadCount(), equalTo(900_000));
                assertThat("TestEndToEnd exportSortedOrdersStep error in writeCount", stepExecution.getWriteCount(), equalTo(900_000));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
                assertThat("TestEndToEnd exportSortedOrdersStep error in processSkipCount", stepExecution.getProcessSkipCount(), equalTo(0));
                assertThat("TestEndToEnd exportSortedOrdersStep error in readSkipCount", stepExecution.getReadSkipCount(), equalTo(0));
            }
        }
    }
}
