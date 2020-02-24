package io.dev.home.salesorders.infrastructure.batch.reader;

import io.dev.home.salesorders.infrastructure.persistence.OrdersFile;
import io.dev.home.salesorders.model.Order;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;

public class OrderLineMapper implements LineMapper<Order> {

    private final long jobExecutionId;

    private final LineTokenizer tokenizer;
    private final FieldSetMapper<Order> fieldSetMapper;

    public OrderLineMapper(long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;

        tokenizer = getLineTokenizer();
        fieldSetMapper = getFieldSetMapper();
    }

    @Override
    public Order mapLine(String line, int lineNumber) throws Exception {
        Order order = fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
        order.setJobExecutionId(jobExecutionId);
        order.setLineNumber(lineNumber);
        return order;
    }

    private DelimitedLineTokenizer getLineTokenizer() {
        return new DelimitedLineTokenizer() {{
            setNames(OrdersFile.HEADER_READ_MAPPING);
            setDelimiter(OrdersFile.DELIMITER);
        }};
    }

    private BeanWrapperFieldSetMapper<Order> getFieldSetMapper() {
        return new BeanWrapperFieldSetMapper<Order>() {{
            setTargetType(Order.class);
        }};
    }
}
