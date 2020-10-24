package uk.co.mruoc.spring.filter.logging.request;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RequestWrapperTest {

    private final RequestWrapper wrapper = new RequestWrapper();

    @Test
    void shouldWrapRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ContentCachingRequestWrapper wrapped = wrapper.wrap(request);

        assertThat(wrapped.getRequest()).isEqualTo(request);
    }

}
