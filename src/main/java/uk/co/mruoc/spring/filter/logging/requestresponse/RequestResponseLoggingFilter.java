package uk.co.mruoc.spring.filter.logging.requestresponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.HeaderExtractor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final RequestResponseWrapper wrapper;
    private final HeaderExtractor extractor;
    private final BodyExtractor bodyExtractor;

    public RequestResponseLoggingFilter() {
        this(new RequestResponseWrapper(), new HeaderExtractor(), new BodyExtractor());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = wrapper.wrap(request);
        ContentCachingResponseWrapper wrappedResponse = wrapper.wrap(response);
        logRequest(wrappedRequest);
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrappedResponse);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        HeaderAdapter headers = extractor.extractHeaders(request);
        String body = bodyExtractor.extractBody(request);
        log.info("received-request:{}:headers:{}", body, headers.asMap());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        HeaderAdapter headers = extractor.extractHeaders(response);
        String body = bodyExtractor.extractBody(response);
        log.info("returned-response:{}:headers:{}", body, headers.asMap());
    }

}
