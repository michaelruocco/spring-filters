package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SimpleRequestBodyExtractor implements RequestBodyExtractor {

    @Override
    public String extractBody(ContentCachingRequestWrapper request) throws IOException {
        return IOUtils.toString(request.getContentAsByteArray(), request.getCharacterEncoding());
    }

}
