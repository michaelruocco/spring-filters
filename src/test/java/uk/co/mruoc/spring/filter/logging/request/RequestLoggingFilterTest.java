package uk.co.mruoc.spring.filter.logging.request;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.RequestHeaderExtractor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.co.mruoc.logging.LogOutputUtils.captureLogLines;

class RequestLoggingFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final RequestWrapper wrapper = mock(RequestWrapper.class);
    private final RequestHeaderExtractor headerExtractor = mock(RequestHeaderExtractor.class);
    private final RequestBodyExtractor bodyExtractor = mock(RequestBodyExtractor.class);

    private final Filter filter = new RequestLoggingFilter(wrapper, headerExtractor, bodyExtractor);

    @Test
    void shouldLogEmptyRequestHeaders() throws Exception {
        ContentCachingRequestWrapper wrappedRequest = givenWrapped(request);
        givenHeaders(wrappedRequest);
        givenBody(wrappedRequest);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] received-request:request-body:headers:{}"
        );
    }

    @Test
    void shouldLogRequestHeaders() throws Exception {
        ContentCachingRequestWrapper wrappedRequest = givenWrapped(request);
        HeaderAdapter requestHeaders = givenHeaders(wrappedRequest);
        given(requestHeaders.asMap()).willReturn(Map.of("Request-Header", Collections.singleton("requestValue")));
        givenBody(wrappedRequest);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] received-request:request-body:headers:{Request-Header=[requestValue]}"
        );
    }

    private ContentCachingRequestWrapper givenWrapped(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapped = mock(ContentCachingRequestWrapper.class);
        given(wrapper.wrap(request)).willReturn(wrapped);
        return wrapped;
    }

    private HeaderAdapter givenHeaders(ContentCachingRequestWrapper request) {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headerExtractor.extractHeaders(request)).willReturn(headers);
        return headers;
    }

    private void givenBody(ContentCachingRequestWrapper request) throws IOException {
        String body = "request-body";
        given(bodyExtractor.extractBody(request)).willReturn(body);
    }

}
