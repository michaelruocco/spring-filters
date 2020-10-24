package uk.co.mruoc.spring.filter.logging.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.json.mask.JsonMasker;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JsonMaskingResponseBodyExtractor implements ResponseBodyExtractor {

    private final JsonMasker jsonMasker;
    private final ResponseBodyExtractor extractor;

    @Override
    public String extractBody(ContentCachingResponseWrapper response) throws IOException {
        String body = extractor.extractBody(response);
        return jsonMasker.mask(body);
    }

}
