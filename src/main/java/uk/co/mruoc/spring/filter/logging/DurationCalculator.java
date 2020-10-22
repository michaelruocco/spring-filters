package uk.co.mruoc.spring.filter.logging;

import java.time.Duration;
import java.time.Instant;

public interface DurationCalculator {

    static long millisBetweenNowAnd(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }

}
