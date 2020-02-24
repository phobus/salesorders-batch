package io.dev.home.salesorders.infrastructure.batch.listener;

import io.dev.home.salesorders.model.ImportError;
import io.dev.home.salesorders.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.Collections;

public class OrderSkipListener implements StepExecutionListener, SkipListener<Order, Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderSkipListener.class);

    public static final String COMPLETED_WITH_SKIPS = "COMPLETED WITH SKIPS";
    public static final String COMPLETED_ALL_SKIPPED = "COMPLETED ALL SKIPPED";

    private long jobExecutionId;

    private final FlatFileItemWriter<ImportError> importErrorItemWriter;

    public OrderSkipListener(FlatFileItemWriter<ImportError> importErrorItemWriter) {
        this.importErrorItemWriter = importErrorItemWriter;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecutionId = stepExecution.getJobExecutionId();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = stepExecution.getExitStatus();

        if (isFailed(exitStatus)) {
            return exitStatus;
        }

        if (skipAll(stepExecution)) {
            exitStatus = new ExitStatus(COMPLETED_ALL_SKIPPED);
            logger.error(COMPLETED_ALL_SKIPPED);
        } else if (hasSkips(stepExecution)) {
            exitStatus = new ExitStatus(COMPLETED_WITH_SKIPS);
            logger.error(COMPLETED_WITH_SKIPS);
        }

        return exitStatus;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        reportError(t);
    }

    @Override
    public void onSkipInProcess(Order item, Throwable t) {
        reportError(item, t);
    }

    @Override
    public void onSkipInWrite(Order item, Throwable t) {
        reportError(item, t);
    }

    private void reportError(Throwable t) {
        Order item = new Order();
        item.setJobExecutionId(jobExecutionId);
        item.setLineNumber(-1);
        reportError(item, t);
    }

    private void reportError(Order item, Throwable t) {
        ImportError importError = new ImportError(
                item.getJobExecutionId(),
                item.getLineNumber(),
                t.getClass().getName()
        );
        try {
            logger.debug("Error '{}' skip line {}.", importError.getExceptionName(), importError.getLineNumber());
            importErrorItemWriter.write(Collections.singletonList(importError));
        } catch (Exception e) {
            logger.error("Error writing orders import error log.", e);
        }
    }

    private boolean skipAll(StepExecution stepExecution) {
        return stepExecution.getSkipCount() == stepExecution.getReadCount();
    }

    private boolean hasSkips(StepExecution stepExecution) {
        return stepExecution.getSkipCount() > 0;
    }

    private boolean isFailed(ExitStatus exitStatus) {
        return exitStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode());
    }
}
