package uk.co.mruoc.spring.filter.logging.response;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.json.mask.JsonMasker;

import java.io.IOException;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TransformingResponseBodyExtractorTest {

    private final UnaryOperator<String> masker = mock(JsonMasker.class);
    private final ResponseBodyExtractor extractor = mock(ResponseBodyExtractor.class);

    private final ResponseBodyExtractor maskingExtractor = new TransformingResponseBodyExtractor(masker, extractor);

    @Test
    void shouldExtractAndMaskBody() throws IOException {
        ContentCachingResponseWrapper response = mock(ContentCachingResponseWrapper.class);
        String body = givenHasBody(response);
        String expectedMaskedBody = givenBodyMaskedTo(body);

        String maskedBody = maskingExtractor.extractBody(response);

        assertThat(maskedBody).isEqualTo(expectedMaskedBody);
    }

    private String givenHasBody(ContentCachingResponseWrapper response) throws IOException {
        String body = "body";
        given(extractor.extractBody(response)).willReturn(body);
        return body;
    }

    private String givenBodyMaskedTo(String body) {
        String maskedBody = "masked-body";
        given(masker.apply(body)).willReturn(maskedBody);
        return maskedBody;
    }

}
