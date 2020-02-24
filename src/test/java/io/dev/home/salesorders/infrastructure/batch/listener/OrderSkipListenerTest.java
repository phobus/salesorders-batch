package io.dev.home.salesorders.infrastructure.batch.listener;

import io.dev.home.salesorders.model.ImportError;
import io.dev.home.salesorders.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static io.dev.home.salesorders.infrastructure.batch.listener.OrderSkipListener.COMPLETED_ALL_SKIPPED;
import static io.dev.home.salesorders.infrastructure.batch.listener.OrderSkipListener.COMPLETED_WITH_SKIPS;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderSkipListenerTest {

    private OrderSkipListener orderSkipListener;

    private FlatFileItemWriter<ImportError> mockWriter;
    private StepExecution mockStepExecution;
    private ArgumentCaptor<List<ImportError>> argumentCaptor;

    private Long jobExecutionId = 1234L;
    private Order order = new Order() {{
        setLineNumber(12);
        setJobExecutionId(jobExecutionId);
    }};
    private Throwable duplicateKeyException = new DuplicateKeyException("test exception");

    @Before
    public void setUp() {
        mockWriter = mock(FlatFileItemWriter.class);
        mockStepExecution = mock(StepExecution.class);
        argumentCaptor = ArgumentCaptor.forClass(List.class);

        when(mockStepExecution.getJobExecutionId()).thenReturn(jobExecutionId);
        orderSkipListener = new OrderSkipListener(mockWriter);
        orderSkipListener.beforeStep(mockStepExecution);
    }

    @Test
    public void testWhenStatusIsFailed_thenAfterStepIsFailed() {
        when(mockStepExecution.getExitStatus()).thenReturn(ExitStatus.FAILED);

        ExitStatus exitStatus = orderSkipListener.afterStep(mockStepExecution);

        assertEquals(exitStatus.getExitCode(), ExitStatus.FAILED.getExitCode());
    }

    @Test
    public void testWhenHasSkips_thenExitStatusIsWithSkips() {
        when(mockStepExecution.getExitStatus()).thenReturn(ExitStatus.COMPLETED);
        when(mockStepExecution.getReadCount()).thenReturn(10);
        when(mockStepExecution.getSkipCount()).thenReturn(3);

        ExitStatus exitStatus = orderSkipListener.afterStep(mockStepExecution);

        assertEquals(exitStatus.getExitCode(), COMPLETED_WITH_SKIPS);
    }

    @Test
    public void testWhenAllSkipped_thenExitStatusIsAllSkipped() {
        when(mockStepExecution.getExitStatus()).thenReturn(ExitStatus.COMPLETED);
        when(mockStepExecution.getReadCount()).thenReturn(10);
        when(mockStepExecution.getSkipCount()).thenReturn(10);

        ExitStatus exitStatus = orderSkipListener.afterStep(mockStepExecution);

        assertEquals(exitStatus.getExitCode(), COMPLETED_ALL_SKIPPED);
    }

    @Test
    public void testWhenSkipInWrite_thenWriteLog() throws Exception {
        orderSkipListener.onSkipInWrite(order, duplicateKeyException);

        Mockito.verify(mockWriter).write(argumentCaptor.capture());
        List<ImportError> importErrors = argumentCaptor.getValue();

        assertThat(importErrors, hasSize(1));
        ImportError importError = importErrors.get(0);
        assertThat(importError, hasProperty("lineNumber", equalTo(order.getLineNumber())));
        assertThat(importError, hasProperty("jobExecutionId", equalTo(order.getJobExecutionId())));
        assertThat(importErrors.get(0), hasProperty("exceptionName", equalTo(duplicateKeyException.getClass().getName())));
    }


    @Test
    public void testWhenSkipInProcess_thenWriteLog() throws Exception {
        orderSkipListener.onSkipInProcess(order, duplicateKeyException);

        Mockito.verify(mockWriter).write(argumentCaptor.capture());
        List<ImportError> importErrors = argumentCaptor.getValue();

        assertThat(importErrors, hasSize(1));
        ImportError importError = importErrors.get(0);
        assertThat(importError, hasProperty("lineNumber", equalTo(order.getLineNumber())));
        assertThat(importError, hasProperty("jobExecutionId", equalTo(order.getJobExecutionId())));
        assertThat(importErrors.get(0), hasProperty("exceptionName", equalTo(duplicateKeyException.getClass().getName())));
    }

    @Test
    public void testWhenSkipInRead_thenWriteLog() throws Exception {
        orderSkipListener.onSkipInRead(duplicateKeyException);

        Mockito.verify(mockWriter).write(argumentCaptor.capture());
        List<ImportError> importErrors = argumentCaptor.getValue();
        assertThat(importErrors, hasSize(1));
        ImportError importError = importErrors.get(0);
        assertThat(importError, hasProperty("lineNumber", equalTo(-1)));
        assertThat(importError, hasProperty("jobExecutionId", equalTo(jobExecutionId)));
        assertThat(importErrors.get(0), hasProperty("exceptionName", equalTo(duplicateKeyException.getClass().getName())));
    }
}