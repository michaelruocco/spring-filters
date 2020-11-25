package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.function.UnaryOperator;

@Slf4j
@RequiredArgsConstructor
public class TransformingRequestBodyExtractor implements RequestBodyExtractor {

    private final UnaryOperator<String> transformer;
    private final RequestBodyExtractor extractor;

    public TransformingRequestBodyExtractor(UnaryOperator<String> transformer) {
        this(transformer, new SimpleRequestBodyExtractor());
    }

    @Override
    public String extractBody(HttpServletRequest request) {
        String body = extractor.extractBody(request);
        return transformer.apply(body);
    }

}
