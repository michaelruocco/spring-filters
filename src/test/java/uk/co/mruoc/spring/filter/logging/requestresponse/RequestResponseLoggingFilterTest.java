package uk.co.mruoc.spring.filter.logging.requestresponse;

import com.github.stefanbirkner.systemlambda.Statement;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.HeaderExtractor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RequestResponseLoggingFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final RequestResponseWrapper wrapper = mock(RequestResponseWrapper.class);
    private final HeaderExtractor headerExtractor = mock(HeaderExtractor.class);
    private final BodyExtractor bodyExtractor = mock(BodyExtractor.class);

    private final Filter filter = new RequestResponseLoggingFilter(wrapper, headerExtractor, bodyExtractor);

    @Test
    void shouldLogEmptyHeaders() throws Exception {
        ContentCachingRequestWrapper wrappedRequest = givenWrapped(request);
        givenHeaders(wrappedRequest);
        givenBody(wrappedRequest);

        ContentCachingResponseWrapper wrappedResponse = givenWrapped(response);
        givenHeaders(wrappedResponse);
        givenBody(wrappedResponse);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] received-request:request-body:headers:{}",
                "INFO [::::::] returned-response:response-body:headers:{}"
        );
    }

    @Test
    void shouldLogHeaders() throws Exception {
        ContentCachingRequestWrapper wrappedRequest = givenWrapped(request);
        HeaderAdapter requestHeaders = givenHeaders(wrappedRequest);
        given(requestHeaders.asMap()).willReturn(Map.of("Request-Header", Collections.singleton("requestValue")));
        givenBody(wrappedRequest);

        ContentCachingResponseWrapper wrappedResponse = givenWrapped(response);
        HeaderAdapter responseHeaders = givenHeaders(wrappedResponse);
        given(responseHeaders.asMap()).willReturn(Map.of("Response-Header", Collections.singleton("responseValue")));
        givenBody(wrappedResponse);

        Collection<String> logLines = captureLogLines(() -> filter.doFilter(request, response, chain));

        assertThat(logLines).containsExactly(
                "INFO [::::::] received-request:request-body:headers:{Request-Header=[requestValue]}",
                "INFO [::::::] returned-response:response-body:headers:{Response-Header=[responseValue]}"
        );
    }

    private ContentCachingRequestWrapper givenWrapped(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapped = mock(ContentCachingRequestWrapper.class);
        given(wrapper.wrap(request)).willReturn(wrapped);
        return wrapped;
    }

    private ContentCachingResponseWrapper givenWrapped(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapped = mock(ContentCachingResponseWrapper.class);
        given(wrapper.wrap(response)).willReturn(wrapped);
        return wrapped;
    }

    private HeaderAdapter givenHeaders(ContentCachingRequestWrapper request) {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headerExtractor.extractHeaders(request)).willReturn(headers);
        return headers;
    }

    private HeaderAdapter givenHeaders(ContentCachingResponseWrapper response) {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headerExtractor.extractHeaders(response)).willReturn(headers);
        return headers;
    }

    private void givenBody(ContentCachingRequestWrapper request) throws IOException {
        String body = "request-body";
        given(bodyExtractor.extractBody(request)).willReturn(body);
    }

    private void givenBody(ContentCachingResponseWrapper response) throws IOException {
        String body = "response-body";
        given(bodyExtractor.extractBody(response)).willReturn(body);
    }

    private static Collection<String> captureLogLines(Statement statement) throws Exception {
        String[] logLines = tapSystemOut(statement).split(System.lineSeparator());
        return Arrays.stream(logLines).collect(Collectors.toList());
    }

}
