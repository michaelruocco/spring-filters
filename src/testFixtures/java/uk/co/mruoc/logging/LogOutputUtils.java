package uk.co.mruoc.logging;

import lombok.extern.slf4j.Slf4j;
import uk.org.webcompere.systemstubs.ThrowingRunnable;
import uk.org.webcompere.systemstubs.stream.SystemOut;

import java.util.Collection;
import java.util.stream.Collectors;

import static uk.org.webcompere.systemstubs.SystemStubs.tapSystemOut;

@Slf4j
public class LogOutputUtils {

    private LogOutputUtils() {
        // utility class
    }

    public static Collection<String> captureLogLines(ThrowingRunnable runnable) throws Exception {
        SystemOut systemOut = new SystemOut();
        systemOut.execute(runnable);
        return systemOut.getLines().collect(Collectors.toList());
    }

    public static String generateLogOutput() throws Exception {
        return tapSystemOut(() -> log.info("test log message"));
    }

}
