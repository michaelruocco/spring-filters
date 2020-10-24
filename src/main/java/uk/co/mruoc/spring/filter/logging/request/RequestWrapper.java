package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class RequestWrapper {

    public ContentCachingRequestWrapper wrap(HttpServletRequest request) {
        return new ContentCachingRequestWrapper(request);
    }

}
