package io.dev.home.salesorders.infrastructure.persistence;

public class OrdersFile {

    public static final int HEADER_LINES = 1;
    public static final String DELIMITER = ",";

    public static final String[] HEADER_READ_MAPPING = {
            "region",
            "country",
            "itemType",
            "salesChannel",
            "orderPriority",
            "orderDate",
            "orderId",
            "shipDate",
            "unitsSold",
            "unitPrice",
            "unitCost",
            "totalRevenue",
            "totalCost",
            "totalProfit"
    };

    public static final String[] HEADER_WRITE_MAPPING = {
            "region",
            "country",
            "itemType",
            "salesChannel",
            "orderPriority",
            "orderDateFormatted",
            "orderId",
            "shipDateFormatted",
            "unitsSold",
            "unitPrice",
            "unitCost",
            "totalRevenue",
            "totalCost",
            "totalProfit"
    };

}

