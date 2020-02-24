package io.dev.home.salesorders.infrastructure.persistence;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OrdersDb {

    public static final String TABLE_NAME = "orders";

    public static final String ORDER_ID = "order_id";
    public static final String REGION = "region";
    public static final String COUNTRY = "country";
    public static final String ITEM_TYPE = "item_type";
    public static final String SALES_CHANNEL = "sales_channel";
    public static final String ORDER_PRIORITY = "order_priority";
    private static final String DATABASE_COLUMNS = String.join(", ", new String[]{
            REGION,
            COUNTRY,
            ITEM_TYPE,
            SALES_CHANNEL,
            ORDER_PRIORITY,
            "order_date",
            ORDER_ID,
            "ship_date",
            "units_sold",
            "unit_price",
            "unit_cost",
            "total_revenue",
            "total_cost",
            "total_profit"
    });

    private static final String DATABASE_PARAMS = Arrays.stream(OrdersFile.HEADER_READ_MAPPING)
            .map(s -> ":" + s)
            .collect(Collectors.joining(", "));

    private static final String SQL_INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String SQL_SELECT_TEMPLATE = "SELECT %s FROM %s WHERE job_execution_id=%d ORDER BY %s";

    public static String createQuery(long jobExecutionId) {
        return String.format(SQL_SELECT_TEMPLATE, DATABASE_COLUMNS, TABLE_NAME, jobExecutionId, ORDER_ID);
    }

    public static String createCommandInsert() {
        return String.format(
                SQL_INSERT_TEMPLATE,
                TABLE_NAME,
                DATABASE_COLUMNS + ", line_number, job_execution_id",
                DATABASE_PARAMS + ", :lineNumber, :jobExecutionId"
        );
    }
}
