package uk.co.mruoc.spring.filter.logging.requestresponse;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RequestResponseWrapperTest {

    private final RequestResponseWrapper wrapper = new RequestResponseWrapper();

    @Test
    void shouldWrapRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ContentCachingRequestWrapper wrapped = wrapper.wrap(request);

        assertThat(wrapped.getRequest()).isEqualTo(request);
    }

    @Test
    void shouldWrapResponse() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ContentCachingResponseWrapper wrapped = wrapper.wrap(response);

        assertThat(wrapped.getResponse()).isEqualTo(response);
    }

}
