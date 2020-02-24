package io.dev.home.salesorders.endtoend;

import io.dev.home.salesorders.batch.TestKit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.dev.home.salesorders.endtoend.TestEndToEndJobImportOrdersTestKit.REGISTRO_VENTAS_1_CSV;
import static io.dev.home.salesorders.endtoend.TestEndToEndJobImportOrdersTestKit.REGISTRO_VENTAS_2_CSV;

@SpringBootTest
@SpringBatchTest
@RunWith(SpringRunner.class)
public class TestEndToEndJobImportOrders {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private EmbeddedDatabase embeddedDatabase;
    private String file1;
    private String file2;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = TestKit.createDatabase();
        file1 = TestKit.copyTemporaryFile(TestKit.getExampleFolder(), REGISTRO_VENTAS_1_CSV, temporaryFolder);
        file2 = TestKit.copyTemporaryFile(TestKit.getExampleFolder(), REGISTRO_VENTAS_2_CSV, temporaryFolder);
    }

    @After
    public void tearDown() throws Exception {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testWhenTheExerciseFilesAreProcessed_resultsAreAsExpected() throws Exception {
        JobParameters jobParameters1 = TestKit.createJobParameters(file1);
        JobParameters jobParameters2 = TestKit.createJobParameters(file2);

        JobExecution jobExecution1 = jobLauncherTestUtils.launchJob(jobParameters1);
        JobExecution jobExecution2 = jobLauncherTestUtils.launchJob(jobParameters2);



        System.out.println(jobExecution2.getExitStatus());
        for (StepExecution stepExecution : jobExecution2.getStepExecutions()) {
            System.out.println();
            System.out.println("getExitStatus" + stepExecution.getExitStatus());
            System.out.println("getReadCount" + stepExecution.getReadCount());
            System.out.println("getWriteCount" + stepExecution.getWriteCount());
            System.out.println("getReadSkipCount" + stepExecution.getReadSkipCount());
            System.out.println("getProcessSkipCount" + stepExecution.getProcessSkipCount());
            System.out.println("getReadSkipCount" + stepExecution.getReadSkipCount());
            System.out.println();
        }
        TestEndToEndJobImportOrdersTestKit.assertThatJobExecution1IsCorrect(jobExecution1);
        TestEndToEndJobImportOrdersTestKit.assertThatJobExecution2IsCorrect(jobExecution2);
    }
}
