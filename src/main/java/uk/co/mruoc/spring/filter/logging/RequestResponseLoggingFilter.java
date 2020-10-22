package uk.co.mruoc.spring.filter.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        Instant start = Instant.now();
        ContentCachingRequestWrapper wrappedRequest = wrap(request);
        ContentCachingResponseWrapper wrappedResponse = wrap(response);
        logRequest(wrappedRequest);
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrappedResponse, DurationCalculator.millisBetweenNowAnd(start));
        }
    }

    private ContentCachingRequestWrapper wrap(HttpServletRequest request) {
        return new ContentCachingRequestWrapper(request);
    }

    private ContentCachingResponseWrapper wrap(HttpServletResponse response) {
        return new ContentCachingResponseWrapper(response);
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        log.info("received-request:{}:headers:{}", extractBody(request), extractHeaders(request));
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) throws IOException {
        MDC.put("duration", Long.toString(duration));
        MDC.put("http-status", Integer.toString(response.getStatus()));
        log.info("returned-response:{}:headers:{}", extractBody(response), extractHeaders(response));
        response.copyBodyToResponse();
    }

    private String extractBody(ContentCachingRequestWrapper request) throws IOException {
        return IOUtils.toString(request.getContentAsByteArray(), request.getCharacterEncoding());
    }

    private Map<String, Collection<String>> extractHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));
    }

    private String extractBody(ContentCachingResponseWrapper response) throws IOException {
        return IOUtils.toString(response.getContentAsByteArray(), response.getCharacterEncoding());
    }

    private Map<String, Collection<String>> extractHeaders(HttpServletResponse response) {
        return response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(Function.identity(), response::getHeaders));
    }

}
