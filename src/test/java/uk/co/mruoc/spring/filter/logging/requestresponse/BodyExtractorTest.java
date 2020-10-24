package uk.co.mruoc.spring.filter.logging.requestresponse;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BodyExtractorTest {

    private final BodyExtractor extractor = new BodyExtractor();

    @Test
    void shouldReturnRequestBody() throws IOException {
        String content = "request-body";
        ContentCachingRequestWrapper request = mock(ContentCachingRequestWrapper.class);
        given(request.getContentAsByteArray()).willReturn(content.getBytes());
        given(request.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);

        String body = extractor.extractBody(request);

        assertThat(body).isEqualTo(content);
    }

    @Test
    void shouldReturnResponseBody() throws IOException {
        String content = "response-body";
        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        given(response.getContentAsByteArray()).willReturn(content.getBytes());
        given(response.getCharacterEncoding()).willReturn(WebUtils.DEFAULT_CHARACTER_ENCODING);

        String body = extractor.extractBody(response);

        assertThat(body).isEqualTo(content);
    }

}
