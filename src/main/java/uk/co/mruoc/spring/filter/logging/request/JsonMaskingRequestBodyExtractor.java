package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.function.UnaryOperator;

@Slf4j
@RequiredArgsConstructor
public class JsonMaskingRequestBodyExtractor implements RequestBodyExtractor {

    private final UnaryOperator<String> masker;
    private final RequestBodyExtractor extractor;

    public JsonMaskingRequestBodyExtractor(UnaryOperator<String> transformer) {
        this(transformer, new SimpleRequestBodyExtractor());
    }

    @Override
    public String extractBody(HttpServletRequest request) throws IOException {
        String body = extractor.extractBody(request);
        return masker.apply(body);
    }

}
