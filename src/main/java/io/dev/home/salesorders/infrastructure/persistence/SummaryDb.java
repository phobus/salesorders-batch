package io.dev.home.salesorders.infrastructure.persistence;

public class SummaryDb {

    private static final String GROUP_BY = String.join(" ", new String[]{
            "SELECT '%s' name, %s value, count(order_id) count",
            "FROM", OrdersDb.TABLE_NAME,
            "WHERE job_execution_id=%d",
            "GROUP BY %s"
    });

    public static String createQuery(long jobExecutionId) {
        String region = String.format(GROUP_BY, OrdersDb.REGION, OrdersDb.REGION, jobExecutionId, OrdersDb.REGION);
        String country = String.format(GROUP_BY, OrdersDb.COUNTRY, OrdersDb.COUNTRY, jobExecutionId, OrdersDb.COUNTRY);
        String itemType = String.format(GROUP_BY, OrdersDb.ITEM_TYPE, OrdersDb.ITEM_TYPE, jobExecutionId, OrdersDb.ITEM_TYPE);
        String salesChanel = String.format(GROUP_BY, OrdersDb.SALES_CHANNEL, OrdersDb.SALES_CHANNEL, jobExecutionId, OrdersDb.SALES_CHANNEL);
        String orderPrioriry = String.format(GROUP_BY, OrdersDb.ORDER_PRIORITY, OrdersDb.ORDER_PRIORITY, jobExecutionId, OrdersDb.ORDER_PRIORITY);
        return String.join(" UNION ALL ", new String[]{
                region,
                country,
                itemType,
                salesChanel,
                orderPrioriry + " ORDER BY name, count DESC, value;",
        });
    }
}
