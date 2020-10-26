package uk.co.mruoc.spring.filter.logging.request;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CachedBodyHttpServletRequestWrapperTest {

    private static final String BODY = "my-body";

    private final HttpServletRequest request = givenHttpServletRequestWithInputStreamBodyContent();

    private final HttpServletRequest cachedRequest = new CachedBodyHttpServletRequestWrapper(request);

    @Test
    void shouldBeAbleToReadInputStreamMultipleTimes() throws IOException {
        assertThat(IOUtils.toString(cachedRequest.getInputStream(), Charset.defaultCharset())).isEqualTo(BODY);
        assertThat(IOUtils.toString(cachedRequest.getInputStream(), Charset.defaultCharset())).isEqualTo(BODY);
    }

    @Test
    void shouldBeAbleToReadReaderMultipleTimes() throws IOException {
        assertThat(IOUtils.toString(cachedRequest.getReader())).isEqualTo(BODY);
        assertThat(IOUtils.toString(cachedRequest.getReader())).isEqualTo(BODY);
    }

    private HttpServletRequest givenHttpServletRequestWithInputStreamBodyContent() {
        try {
            HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getInputStream()).willReturn(new CachedBodyServletInputStream(BODY.getBytes()));
            given(request.getContentLength()).willReturn(BODY.getBytes().length);
            return request;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
