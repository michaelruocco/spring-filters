package uk.co.mruoc.spring.filter.logging.request;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;
import uk.co.mruoc.json.mask.JsonMasker;

import java.io.IOException;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JsonMaskingRequestBodyExtractorTest {

    private final UnaryOperator<String> masker = mock(JsonMasker.class);
    private final RequestBodyExtractor extractor = mock(RequestBodyExtractor.class);

    private final RequestBodyExtractor maskingExtractor = new JsonMaskingRequestBodyExtractor(masker, extractor);

    @Test
    void shouldExtractAndMaskBody() throws IOException {
        ContentCachingRequestWrapper request = mock(ContentCachingRequestWrapper.class);
        String body = givenHasBody(request);
        String expectedMaskedBody = givenBodyMaskedTo(body);

        String maskedBody = maskingExtractor.extractBody(request);

        assertThat(maskedBody).isEqualTo(expectedMaskedBody);
    }

    private String givenHasBody(ContentCachingRequestWrapper request) throws IOException {
        String body = "body";
        given(extractor.extractBody(request)).willReturn(body);
        return body;
    }

    private String givenBodyMaskedTo(String body) {
        String maskedBody = "masked-body";
        given(masker.apply(body)).willReturn(maskedBody);
        return maskedBody;
    }

}
