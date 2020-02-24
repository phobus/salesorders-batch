package io.dev.home.salesorders.model;

import java.io.Serializable;

public class ImportError implements Serializable {

    private static final long serialVersionUID = 7919631878607326954L;

    private int lineNumber;
    private long jobExecutionId;
    private String exceptionName;

    public ImportError() {
    }

    public ImportError(Long jobExecutionId, Integer lineNumber, String exceptionName) {
        this.jobExecutionId = jobExecutionId;
        this.lineNumber = lineNumber;
        this.exceptionName = exceptionName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ImportError{");
        sb.append("lineNumber=").append(lineNumber);
        sb.append(", jobExecutionId=").append(jobExecutionId);
        sb.append(", exceptionName='").append(exceptionName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
