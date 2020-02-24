package io.dev.home.salesorders.interfaces.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static io.dev.home.salesorders.infrastructure.batch.FileJobParametersValidator.*;

public class ImportOrdersParametersBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ImportOrdersParametersBuilder.class);

    public static final String ERROR_INVALID_ARGUMENT = "Error invalid argument.";
    public static final String FIRST_ARGUMENT_NEED_TO_BE_A_VALID_FILE_PATH = "First argument need to be a valid file path.";
    public static final String EXAMPLE_JAVA = "- Example:";
    public static final String EXAMPLE_JAVA_COMMAND ="java -jar salesorders-batch.jar /path/to/file.csv";

    public static final String PARAMETER_TEMPLATE = "%s=%s%s";

    public static final String EXTENSION_ORDERED_CSV = ".ordered.csv";
    public static final String EXTENSION_SUMMARY_TXT = ".summary.txt";
    public static final String EXTENSION_ERRORS_CSV = ".errors.csv";

    public static String[] createFromArgs(String file) {
        return new String[]{
                String.format(PARAMETER_TEMPLATE, BATCH_FILE_INPUT, file, ""),
                String.format(PARAMETER_TEMPLATE, BATCH_FILE_OUTPUT, file, EXTENSION_ORDERED_CSV),
                String.format(PARAMETER_TEMPLATE, BATCH_FILE_SUMMARY, file, EXTENSION_SUMMARY_TXT),
                String.format(PARAMETER_TEMPLATE, BATCH_FILE_ERROR, file, EXTENSION_ERRORS_CSV),
        };
    }

    public static void validation(String[] args) {
        if (args.length != 1) {
            System.out.println(ERROR_INVALID_ARGUMENT);
            printUsage();
            logger.error(ERROR_INVALID_ARGUMENT + " args=" + Arrays.toString(args));
            throw new RuntimeException(ERROR_INVALID_ARGUMENT);
        }
    }

    private static void printUsage() {
        System.out.println(FIRST_ARGUMENT_NEED_TO_BE_A_VALID_FILE_PATH);
        System.out.println();
        System.out.println(EXAMPLE_JAVA);
        System.out.println();
        System.out.println(EXAMPLE_JAVA_COMMAND);
        System.out.println();
    }
}