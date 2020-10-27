package uk.co.mruoc.spring.filter.logging.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.function.UnaryOperator;

@Slf4j
@RequiredArgsConstructor
public class TransformingResponseBodyExtractor implements ResponseBodyExtractor {

    private final UnaryOperator<String> transformer;
    private final ResponseBodyExtractor extractor;

    public TransformingResponseBodyExtractor(UnaryOperator<String> transformer) {
        this(transformer, new SimpleResponseBodyExtractor());
    }

    @Override
    public String extractBody(ContentCachingResponseWrapper response) throws IOException {
        String body = extractor.extractBody(response);
        return transformer.apply(body);
    }

}
