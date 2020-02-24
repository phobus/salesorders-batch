package io.dev.home.salesorders.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Serializable {

    private static final long serialVersionUID = 4524832057105141292L;

    public static final String DATE_FORMAT_TEMPLATE = "M/d/yyyy";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TEMPLATE);

    @NotEmpty
    private String region;
    @NotEmpty
    private String country;
    @NotEmpty
    private String itemType;
    @NotEmpty
    private String salesChannel;
    @NotEmpty
    private String orderPriority;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_FORMAT_TEMPLATE)
    private Date orderDate;
    private Long orderId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = DATE_FORMAT_TEMPLATE)
    private Date shipDate;
    private int unitsSold;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;

    private Integer lineNumber;
    private Long jobExecutionId;

    public Order() {
    }

    public String getOrderDateFormatted() {
        return dateFormat.format(getOrderDate());
    }

    public String getShipDateFormatted() {
        return dateFormat.format(getShipDate());
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("region='").append(region).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", itemType='").append(itemType).append('\'');
        sb.append(", salesChannel='").append(salesChannel).append('\'');
        sb.append(", orderPriority='").append(orderPriority).append('\'');
        sb.append(", orderDate=").append(orderDate);
        sb.append(", orderID=").append(orderId);
        sb.append(", shipDate=").append(shipDate);
        sb.append(", unitsSold=").append(unitsSold);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", unitCost=").append(unitCost);
        sb.append(", totalRevenue=").append(totalRevenue);
        sb.append(", totalCost=").append(totalCost);
        sb.append(", totalProfit=").append(totalProfit);
        sb.append('}');
        return sb.toString();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }
}
