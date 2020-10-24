package uk.co.mruoc.logging;

import com.github.stefanbirkner.systemlambda.Statement;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;

@Slf4j
public class LogOutputUtils {

    private LogOutputUtils() {
        // utility class
    }

    public static Collection<String> captureLogLines(Statement statement) throws Exception {
        String[] logLines = tapSystemOut(statement).split(System.lineSeparator());
        return Arrays.stream(logLines).collect(Collectors.toList());
    }

    public static String generateLogOutput() throws Exception {
        return tapSystemOut(() -> log.info("test log message"));
    }

}
