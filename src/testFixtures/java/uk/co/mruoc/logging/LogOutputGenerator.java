package uk.co.mruoc.logging;

import lombok.extern.slf4j.Slf4j;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

@Slf4j
public class LogOutputGenerator {

    private LogOutputGenerator() {
        // utility class
    }

    public static String generateLogOutput() throws Exception {
        return tapSystemOut(() -> log.info("test log message"));
    }

}
