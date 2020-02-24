package io.dev.home.salesorders.infrastructure.batch;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

import java.io.File;
import java.util.Map;

public class FileJobParametersValidator implements JobParametersValidator {

    public static final String BATCH_FILE_INPUT = "batch.file.input";
    public static final String BATCH_FILE_OUTPUT = "batch.file.output";
    public static final String BATCH_FILE_SUMMARY = "batch.file.summary";
    public static final String BATCH_FILE_ERROR = "batch.file.error";

    public static final String[] REQUIRED_KEYS = {
            BATCH_FILE_INPUT,
            BATCH_FILE_OUTPUT,
            BATCH_FILE_SUMMARY,
            BATCH_FILE_ERROR
    };

    public static final String[] OPTIONAL_KEYS = {};

    private DefaultJobParametersValidator defaultJobParametersValidator;

    public FileJobParametersValidator() {
        defaultJobParametersValidator = new DefaultJobParametersValidator(REQUIRED_KEYS, OPTIONAL_KEYS);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        defaultJobParametersValidator.validate(parameters);
        Map<String, JobParameter> map = parameters.getParameters();
        validateInput(map.get(BATCH_FILE_INPUT));
        validateOutput(map.get(BATCH_FILE_OUTPUT));
        validateOutput(map.get(BATCH_FILE_SUMMARY));
        validateOutput(map.get(BATCH_FILE_ERROR));
    }

    private void validateInput(JobParameter jobParameter) throws JobParametersInvalidException {
        String path = (String) jobParameter.getValue();
        File file = new File(path);

        if (!file.exists() || file.isDirectory()) {
            throw new JobParametersInvalidException(BATCH_FILE_INPUT + " is not valid file. " + path);
        }

    }

    private void validateOutput(JobParameter jobParameter) {
    }
}
