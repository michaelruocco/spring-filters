package uk.co.mruoc.spring.filter.logging.response;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SimpleResponseBodyExtractorTest {

    private final ResponseBodyExtractor extractor = new SimpleResponseBodyExtractor();

    @Test
    void shouldReturnResponseBody() throws IOException {
        String content = "response-body";
        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        given(response.getContentAsByteArray()).willReturn(content.getBytes());
        given(response.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);

        String body = extractor.extractBody(response);

        assertThat(body).isEqualTo(content);
        verify(response).copyBodyToResponse();
    }

}
