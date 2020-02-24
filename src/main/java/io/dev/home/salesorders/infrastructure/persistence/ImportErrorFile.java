package io.dev.home.salesorders.infrastructure.persistence;

public class ImportErrorFile {
    public static final String[] HEADER_MAPPING = {"lineNumber", "exceptionName"};
    public static final String HEADER_MAPPING_LINE = String.join(", ", HEADER_MAPPING);
}
