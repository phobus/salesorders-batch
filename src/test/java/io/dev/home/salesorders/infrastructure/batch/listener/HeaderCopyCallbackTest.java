package io.dev.home.salesorders.infrastructure.batch.listener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class HeaderCopyCallbackTest {

    public static final String TEST_HEADER_LINE = "value1, Value2, VALUE 3";

    private Writer mockWriter;
    private ArgumentCaptor<String> argumentCaptor;

    @Before
    public void setUp() {
        mockWriter = mock(Writer.class);
        argumentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    public void testWhenHandleLine_thenWriteHeaderAreEquals() throws IOException {
        HeaderCopyCallback headerCopyCallback = new HeaderCopyCallback();

        headerCopyCallback.handleLine(TEST_HEADER_LINE);
        headerCopyCallback.writeHeader(mockWriter);

        Mockito.verify(mockWriter).write(argumentCaptor.capture());
        assertEquals(TEST_HEADER_LINE, argumentCaptor.getValue());
    }
}