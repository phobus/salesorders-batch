package io.dev.home.salesorders.model;

import java.io.Serializable;

public class SummaryLine implements Serializable {

    private static final long serialVersionUID = 2790657557766579928L;

    private String name;

    private String value;

    private int count;

    public SummaryLine() {
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Summary{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
