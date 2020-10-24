package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import uk.co.mruoc.json.mask.JsonMasker;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JsonMaskingRequestBodyExtractor implements RequestBodyExtractor {

    private final JsonMasker jsonMasker;
    private final RequestBodyExtractor extractor;

    @Override
    public String extractBody(ContentCachingRequestWrapper request) throws IOException {
        String body = extractor.extractBody(request);
        return jsonMasker.mask(body);
    }

}
