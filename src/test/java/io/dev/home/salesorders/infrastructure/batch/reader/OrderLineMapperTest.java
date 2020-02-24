package io.dev.home.salesorders.infrastructure.batch.reader;

import io.dev.home.salesorders.model.Order;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.*;

public class OrderLineMapperTest {

    private long jobExecutionId = 1234L;
    private int lineNumber = 64;
    private String line = "Asia,Maldives,Fruits,Offline,L,1/8/2011,714135205,2/6/2011,7332,9.33,6.92,68407.56,50737.44,17670.12";

    @Test
    public void testWhenMapLine_thenOrderHaveLineNumber() throws Exception {
        OrderLineMapper orderLineMapper = new OrderLineMapper(jobExecutionId);

        Order order = orderLineMapper.mapLine(line, lineNumber);

        assertThat(order, hasProperty("lineNumber", equalTo(lineNumber)));
    }

    @Test
    public void testWhenMapLine_thenOrderHaveJobExecutionId() throws Exception {
        OrderLineMapper orderLineMapper = new OrderLineMapper(jobExecutionId);

        Order order = orderLineMapper.mapLine(line, lineNumber);

        assertThat(order, hasProperty("jobExecutionId", equalTo(jobExecutionId)));
    }
}