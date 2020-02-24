package io.dev.home.salesorders.batch;

import io.dev.home.salesorders.config.BatchConfig;
import io.dev.home.salesorders.endtoend.TestEndToEndJobImportOrdersTestKit;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.*;

public class TestKit {

    public static final String SALES_ORDERS_CSV = "salesOrders.csv";
    public static final String ONLY_HEADER_CSV = "onlyHeader.csv";
    public static final String EMPTY_FILE_CSV = "emptyFile.csv";
    public static final String DUPLICATE_LINE_CSV = "duplicateLine.csv";

    public static final String SQL_PROJECT_SCHEMA_SQL = "sql/project-schema.sql";

    public static EmbeddedDatabase createDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(SQL_PROJECT_SCHEMA_SQL)
                .build();
    }

    public static JobParameters createJobParameters(String file) {
        return new JobParametersBuilder()
                .addString(BATCH_FILE_INPUT, file)
                .addString(BATCH_FILE_OUTPUT, file + ".ordered.csv")
                .addString(BATCH_FILE_SUMMARY, file + ".summary.txt")
                .addString(BATCH_FILE_ERROR, file + ".errors.csv")
                .toJobParameters();
    }

    public static String getFileFolder() {
        return TestEndToEndJobImportOrdersTestKit.class.getClassLoader().getResource("file").getFile() + File.separatorChar;
    }

    public static String getExampleFolder() {
        return TestEndToEndJobImportOrdersTestKit.class.getClassLoader().getResource("example").getFile() + File.separatorChar;
    }

    public static String copyTemporaryFile(String source, String file, TemporaryFolder temporaryFolder) {
        String dest = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + file;
        try {
            Files.copy(Paths.get(source + file), Paths.get(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public static String getSalesOrderResultOrdered() {
        return TestEndToEndJobImportOrdersTestKit.class.getClassLoader().getResource("results/salesOrders.csv.ordered.csv").getFile();
    }
}
