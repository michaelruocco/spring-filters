package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.RequestHeaderExtractor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestWrapper wrapper;
    private final RequestHeaderExtractor extractor;
    private final RequestBodyExtractor bodyExtractor;

    public RequestLoggingFilter(RequestBodyExtractor bodyExtractor) {
        this(new RequestWrapper(), new RequestHeaderExtractor(), bodyExtractor);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = wrapper.wrap(request);
        logRequest(wrappedRequest);
        chain.doFilter(wrappedRequest, response);
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        HeaderAdapter headers = extractor.extractHeaders(request);
        String body = bodyExtractor.extractBody(request);
        log.info("received-request:{}:headers:{}", body, headers.asMap());
    }

}
