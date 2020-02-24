package io.dev.home.salesorders.infrastructure.batch;

import io.dev.home.salesorders.batch.TestKit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.io.File;

import static org.junit.Assert.*;

public class FileJobParametersValidatorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String fileWithoutExtension;
    private String fileWithExtensionThatExist;
    private String fileThatNotExist;

    @Before
    public void setUp() throws Exception {
        fileWithoutExtension = temporaryFolder.newFile("fileThatExist").getAbsolutePath();
        fileWithExtensionThatExist = temporaryFolder.newFile("fileThatExist.csv").getAbsolutePath();
        fileThatNotExist = "/file/that/not/exist";
    }

    @Test
    public void testWhenValidateFileWithoutExtension_thenNoException() throws JobParametersInvalidException {
        FileJobParametersValidator fileJobParametersValidator = new FileJobParametersValidator();
        JobParameters jobParameters = TestKit.createJobParameters(fileWithoutExtension);

        fileJobParametersValidator.validate(jobParameters);

        assertTrue(true);
    }

    @Test
    public void testWhenValidateFileThatExist_thenNoException() throws JobParametersInvalidException {
        FileJobParametersValidator fileJobParametersValidator = new FileJobParametersValidator();
        JobParameters jobParameters = TestKit.createJobParameters(fileWithExtensionThatExist);

        fileJobParametersValidator.validate(jobParameters);

        assertTrue(true);
    }

    @Test(expected = JobParametersInvalidException.class)
    public void testWhenValidateFileThatNotExist_thenThrowException() throws JobParametersInvalidException {
        FileJobParametersValidator fileJobParametersValidator = new FileJobParametersValidator();
        JobParameters jobParameters = TestKit.createJobParameters(fileThatNotExist);

        fileJobParametersValidator.validate(jobParameters);

        assertTrue(true);
    }
}