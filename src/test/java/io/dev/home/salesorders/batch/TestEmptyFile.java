package io.dev.home.salesorders.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.dev.home.salesorders.batch.TestKit.EMPTY_FILE_CSV;

@SpringBootTest
@SpringBatchTest
@RunWith(SpringRunner.class)
public class TestEmptyFile {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private EmbeddedDatabase embeddedDatabase;
    private String file;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = TestKit.createDatabase();
        file = TestKit.copyTemporaryFile(TestKit.getFileFolder(), EMPTY_FILE_CSV, temporaryFolder);
    }

    @After
    public void tearDown() throws Exception {
        embeddedDatabase.shutdown();
    }

    @Test//( expected = org.springframework.batch.item.ItemStreamException.class)
    public void testWhenFileIsEmpty_then() throws Exception {
//        expectedException.expect(org.springframework.batch.item.ItemStreamException.class);
        JobParameters jobParameters = TestKit.createJobParameters(file);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        List<Throwable> failureExceptions = jobExecution.getFailureExceptions();
    }
}
