package uk.co.mruoc.spring.filter.logging.response;

import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public interface ResponseBodyExtractor {

    String extractBody(ContentCachingResponseWrapper response) throws IOException;

}
