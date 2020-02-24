package io.dev.home.salesorders.infrastructure.batch.writer;

import io.dev.home.salesorders.model.SummaryLine;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.util.Collections;

public class SummaryLineAggregator implements LineAggregator<SummaryLine> {

    public static final String UNDERLINE = "=";
    public static final String TITLE_FORMAT = "Summary %s";
    public static final String TITLE_UNDERLINE_FORMAT = "\n%s\n%s\n";
    public static final String LINE_FORMAT = "%40s %,d";

    private String lastSummary = "";

    @Override
    public String aggregate(SummaryLine item) {
        String line = null;
        if (!lastSummary.equals(item.getName())) {
            line = aggregateTitle(item) + aggregateLine(item);
            lastSummary = item.getName();
        } else {
            line = aggregateLine(item);
        }
        return line;
    }

    private String aggregateTitle(SummaryLine item) {
        String title = createTitle(item);
        return String.format(TITLE_UNDERLINE_FORMAT, title, createUnderline(title));
    }

    private String createTitle(SummaryLine item) {
        return String.format(TITLE_FORMAT, item.getName());
    }

    private String createUnderline(String title) {
        return String.join("", Collections.nCopies(title.length(), UNDERLINE));
    }

    private String aggregateLine(SummaryLine item) {
        return String.format(LINE_FORMAT, item.getValue(), item.getCount());
    }
}
