package uk.co.mruoc.spring.filter.logging.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ResponseLoggingFilter extends OncePerRequestFilter {

    private final ResponseWrapper wrapper;
    private final ResponseHeaderExtractor extractor;
    private final ResponseBodyExtractor bodyExtractor;

    public ResponseLoggingFilter(ResponseBodyExtractor bodyExtractor) {
        this(new ResponseWrapper(), new ResponseHeaderExtractor(), bodyExtractor);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        ContentCachingResponseWrapper wrappedResponse = wrapper.wrap(response);
        try {
            chain.doFilter(request, wrappedResponse);
        } finally {
            logResponse(wrappedResponse);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        HeaderAdapter headers = extractor.extractHeaders(response);
        String body = bodyExtractor.extractBody(response);
        log.info("returned-response:{}:headers:{}", body, headers.asMap());
    }

}
