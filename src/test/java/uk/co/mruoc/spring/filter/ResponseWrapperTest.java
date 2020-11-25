package uk.co.mruoc.spring.filter;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.rewrite.CapturingResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ResponseWrapperTest {

    private final ResponseWrapper wrapper = new ResponseWrapper();

    @Test
    void shouldWrapResponseWithCachingResponseWrapper() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ContentCachingResponseWrapper wrapped = wrapper.toCachingResponseWrapper(response);

        assertThat(wrapped.getResponse()).isEqualTo(response);
    }

    @Test
    void shouldWrapResponseWithCapturingResponse() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        CapturingResponse capturing = wrapper.toCapturingResponse(response);

        assertThat(capturing.getResponse()).isEqualTo(response);
    }

}
