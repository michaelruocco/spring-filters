package uk.co.mruoc.spring.filter.logging.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SimpleResponseBodyExtractor implements ResponseBodyExtractor {

    @Override
    public String extractBody(ContentCachingResponseWrapper response) throws IOException {
        String body = IOUtils.toString(response.getContentAsByteArray(), response.getCharacterEncoding());
        response.copyBodyToResponse();
        return body;
    }

}
