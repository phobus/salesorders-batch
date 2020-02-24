package io.dev.home.salesorders.infrastructure.batch.listener;

import io.dev.home.salesorders.model.Order;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Writer;

public class HeaderCopyCallback implements LineCallbackHandler, FlatFileHeaderCallback {

    private String header = "";

    @Override
    public void handleLine(String line) {
        Assert.notNull(line, "line must not be null");
        this.header = line;
    }

    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
    }
}
