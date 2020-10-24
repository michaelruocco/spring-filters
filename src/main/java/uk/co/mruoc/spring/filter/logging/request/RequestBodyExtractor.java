package uk.co.mruoc.spring.filter.logging.request;

import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

public interface RequestBodyExtractor {

    String extractBody(ContentCachingRequestWrapper request) throws IOException;

}
