package uk.co.mruoc.clock;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FixedTimesClock extends Clock {

    private final List<Instant> values;
    private final AtomicInteger index = new AtomicInteger();

    public FixedTimesClock(Instant... values) {
        this(Arrays.asList(values));
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        Instant value = values.get(index.get());
        incrementOrResetIndex();
        return value;
    }

    private void incrementOrResetIndex() {
        int value = index.incrementAndGet();
        if (value >= values.size()) {
            index.set(0);
        }
    }

}
