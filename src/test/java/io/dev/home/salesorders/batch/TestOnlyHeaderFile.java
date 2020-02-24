package io.dev.home.salesorders.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import static io.dev.home.salesorders.batch.TestKit.ONLY_HEADER_CSV;
import static io.dev.home.salesorders.config.ImportOrdersConfig.IMPORT_ORDERS_STEP;
import static io.dev.home.salesorders.infrastructure.batch.listener.OrderSkipListener.COMPLETED_ALL_SKIPPED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
@SpringBatchTest
@RunWith(SpringRunner.class)
public class TestOnlyHeaderFile {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private EmbeddedDatabase embeddedDatabase;
    private String file;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = TestKit.createDatabase();
        file = TestKit.copyTemporaryFile(TestKit.getFileFolder(), ONLY_HEADER_CSV, temporaryFolder);
    }

    @After
    public void tearDown() throws Exception {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testWhenOnlyHeaderFile_thenSkipAll() throws Exception {
        JobParameters jobParameters = TestKit.createJobParameters(file);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getExitStatus(), equalTo(ExitStatus.COMPLETED));
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            if (IMPORT_ORDERS_STEP.equals(stepExecution.getStepName())) {
                assertThat(stepExecution.getExitStatus().getExitCode(), equalTo(COMPLETED_ALL_SKIPPED));
            }
        }
    }
}
