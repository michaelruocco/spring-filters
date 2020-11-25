package uk.co.mruoc.spring.filter.logging.response;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.ResponseWrapper;

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

class ResponseLoggingFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final ResponseWrapper wrapper = mock(ResponseWrapper.class);
    private final ResponseHeaderExtractor headerExtractor = mock(ResponseHeaderExtractor.class);
    private final ResponseBodyExtractor bodyExtractor = mock(ResponseBodyExtractor.class);

    private final Filter filter = new ResponseLoggingFilter(wrapper, headerExtractor, bodyExtractor);

    @Test
    void shouldLogEmptyResponseHeaders() throws Exception {
        ContentCachingResponseWrapper wrappedResponse = givenWrapped(response);
        givenHeaders(wrappedResponse);
        givenBody(wrappedResponse);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] returned-response:response-body:headers:{}"
        );
    }

    @Test
    void shouldLogResponseHeaders() throws Exception {
        ContentCachingResponseWrapper wrappedResponse = givenWrapped(response);
        HeaderAdapter responseHeaders = givenHeaders(wrappedResponse);
        given(responseHeaders.asMap()).willReturn(Map.of("Response-Header", Collections.singleton("responseValue")));
        givenBody(wrappedResponse);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] returned-response:response-body:headers:{Response-Header=[responseValue]}"
        );
    }

    private ContentCachingResponseWrapper givenWrapped(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapped = mock(ContentCachingResponseWrapper.class);
        given(wrapper.toCachingResponseWrapper(response)).willReturn(wrapped);
        return wrapped;
    }

    private HeaderAdapter givenHeaders(ContentCachingResponseWrapper response) {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headerExtractor.extractHeaders(response)).willReturn(headers);
        return headers;
    }

    private void givenBody(ContentCachingResponseWrapper response) throws IOException {
        String body = "response-body";
        given(bodyExtractor.extractBody(response)).willReturn(body);
    }

}
