package io.dev.home.salesorders.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import static io.dev.home.salesorders.batch.TestKit.SALES_ORDERS_CSV;
import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.BATCH_FILE_OUTPUT;

@SpringBootTest
@SpringBatchTest
@RunWith(SpringRunner.class)
public class TestSalesOrderFile {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private EmbeddedDatabase embeddedDatabase;
    private String file;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = TestKit.createDatabase();
        file = TestKit.copyTemporaryFile(TestKit.getFileFolder(), SALES_ORDERS_CSV, temporaryFolder);
    }

    @After
    public void tearDown() throws Exception {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testWhenFileIsEmpty_then() throws Exception {
        JobParameters jobParameters = TestKit.createJobParameters(file);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        String expected = TestKit.getSalesOrderResultOrdered();
        String output = (String) jobParameters.getParameters().get(BATCH_FILE_OUTPUT).getValue();
        AssertFile.assertFileEquals(new FileSystemResource(expected), new FileSystemResource(output));
    }
}
