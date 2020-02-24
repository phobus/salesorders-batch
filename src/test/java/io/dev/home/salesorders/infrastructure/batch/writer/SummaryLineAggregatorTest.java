package io.dev.home.salesorders.infrastructure.batch.writer;

import io.dev.home.salesorders.model.SummaryLine;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class SummaryLineAggregatorTest {

    private SummaryLine summaryLine1 = new SummaryLine() {{
        setName("test");
        setValue("value1");
        setCount(100000);
    }};


    private SummaryLine summaryLine2 = new SummaryLine() {{
        setName("test");
        setValue("value2");
        setCount(10000);
    }};

    @Test
    public void testWhenSummaryStart_thenAggregateTitleAndLine() {
        SummaryLineAggregator summaryLineAggregator = new SummaryLineAggregator();

        String line = summaryLineAggregator.aggregate(summaryLine1);

        assertThat(line, containsString("Summary test"));
        assertThat(line, containsString("value1"));
        assertThat(line, containsString("100.000"));
    }


    @Test
    public void testWhenAggregateSummaryLine_thenAddLineAndNoTitle() {
        SummaryLineAggregator summaryLineAggregator = new SummaryLineAggregator();

        summaryLineAggregator.aggregate(summaryLine1);
        String line2 = summaryLineAggregator.aggregate(summaryLine2);

        assertThat(line2, not(containsString("Summary test")));
        assertThat(line2, containsString("value2"));
        assertThat(line2, containsString("10.000"));

        assertThat(line2, not(containsString("value1")));
        assertThat(line2, not(containsString("100.000")));
    }
}