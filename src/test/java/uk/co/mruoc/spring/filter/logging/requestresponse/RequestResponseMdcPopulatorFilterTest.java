package uk.co.mruoc.spring.filter.logging.requestresponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import uk.co.mruoc.clock.FixedTimesClock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.co.mruoc.logging.LogOutputGenerator.generateLogOutput;

class RequestResponseMdcPopulatorFilterTest {

    private static final Duration DURATION = Duration.ofSeconds(1);
    private static final Instant NOW = Instant.now();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final FixedTimesClock clock = new FixedTimesClock(NOW, NOW.plus(DURATION));
    private final Filter filter = new RequestResponseMdcPopulatorFilter(clock);

    @BeforeEach
    public void setUp() {
        MDC.clear();
    }

    @AfterEach
    public void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldPopulateRequestMethodMdcContext() throws Exception {
        String method = "POST";
        given(request.getMethod()).willReturn(method);

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [::%s::1000:0:] test log message", method);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).contains(entry("request-method", method));
    }

    @Test
    void shouldPopulateRequestUriMdcContext() throws Exception {
        String uri = "/my-resources";
        given(request.getRequestURI()).willReturn(uri);

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [:::%s:1000:0:] test log message", uri);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).contains(entry("request-uri", uri));
    }

    @Test
    void shouldPopulateRequestDurationInMillisInMdcContext() throws Exception {
        String expectedDuration = Long.toString(DURATION.toMillis());

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [::::%s:0:] test log message", expectedDuration);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).contains(entry("request-duration", expectedDuration));
    }

    @Test
    void shouldPopulateHttpStatusInMdcContext() throws Exception {
        String status = "404";
        given(response.getStatus()).willReturn(Integer.parseInt(status));

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [::::1000:%s:] test log message", status);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).contains(entry("http-status", status));
    }

}
