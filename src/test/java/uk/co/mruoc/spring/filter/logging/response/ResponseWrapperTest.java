package uk.co.mruoc.spring.filter.logging.response;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ResponseWrapperTest {

    private final ResponseWrapper wrapper = new ResponseWrapper();

    @Test
    void shouldWrapResponse() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ContentCachingResponseWrapper wrapped = wrapper.wrap(response);

        assertThat(wrapped.getResponse()).isEqualTo(response);
    }

}
