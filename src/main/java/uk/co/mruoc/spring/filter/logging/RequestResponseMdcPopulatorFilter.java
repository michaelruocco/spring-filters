package uk.co.mruoc.spring.filter.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class RequestResponseMdcPopulatorFilter extends OncePerRequestFilter {

    private final Clock clock;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        Instant start = clock.instant();
        try {
            populateValues(request);
            chain.doFilter(request, response);
        } finally {
            populateValues(response, millisBetweenNowAnd(start));
        }
    }

    private void populateValues(HttpServletRequest request) {
        MDC.put("request-method", request.getMethod());
        MDC.put("request-uri", request.getRequestURI());
    }

    private void populateValues(HttpServletResponse response, long duration) {
        MDC.put("request-duration", Long.toString(duration));
        MDC.put("http-status", Integer.toString(response.getStatus()));
    }

    private long millisBetweenNowAnd(Instant start) {
        return Duration.between(start, clock.instant()).toMillis();
    }

}
