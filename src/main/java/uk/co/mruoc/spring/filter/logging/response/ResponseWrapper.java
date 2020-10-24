package uk.co.mruoc.spring.filter.logging.response;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ResponseWrapper {

    public ContentCachingResponseWrapper wrap(HttpServletResponse response) {
        return new ContentCachingResponseWrapper(response);
    }

}
