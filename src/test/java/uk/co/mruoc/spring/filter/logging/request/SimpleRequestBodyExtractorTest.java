package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SimpleRequestBodyExtractorTest {

    private final RequestBodyExtractor extractor = new SimpleRequestBodyExtractor();

    @Test
    void shouldReturnRequestBody() throws IOException {
        String content = "request-body";
        ContentCachingRequestWrapper request = mock(ContentCachingRequestWrapper.class);
        given(request.getInputStream()).willReturn(new FakeServletInputStream(content.getBytes()));
        given(request.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);

        String body = extractor.extractBody(request);

        assertThat(body).isEqualTo(content);
    }

    @RequiredArgsConstructor
    private static class FakeServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream stream;

        public FakeServletInputStream(byte[] bytes) {
            this(new ByteArrayInputStream(bytes));
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            // intentionally blank
        }

        @Override
        public int read() {
            return stream.read();
        }

    }

}
