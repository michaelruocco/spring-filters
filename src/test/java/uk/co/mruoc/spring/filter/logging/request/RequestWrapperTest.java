package uk.co.mruoc.spring.filter.logging.request;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RequestWrapperTest {

    private final RequestWrapper wrapper = new RequestWrapper();

    @Test
    void shouldWrapRequestIfRequestHasContent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getContentLength()).willReturn(0);

        HttpServletRequest wrapped = wrapper.wrapIfHasContent(request);

        assertThat(wrapped).isInstanceOf(CachedBodyHttpServletRequestWrapper.class);
        CachedBodyHttpServletRequestWrapper cachedWrapper = (CachedBodyHttpServletRequestWrapper) wrapped;
        assertThat(cachedWrapper.getRequest()).isEqualTo(request);
    }

    @Test
    void shouldReturnOriginalRequestIfRequestDoesNotHaveContent() {
        HttpServletRequest expectedRequest = mock(HttpServletRequest.class);
        given(expectedRequest.getContentLength()).willReturn(-1);

        HttpServletRequest request = wrapper.wrapIfHasContent(expectedRequest);

        assertThat(request).isEqualTo(expectedRequest);
    }

}
