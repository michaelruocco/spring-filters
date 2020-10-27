package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class RequestWrapper {

    public HttpServletRequest wrapIfHasContent(HttpServletRequest request) {
        if (hasContent(request)) {
            return new CachedBodyHttpServletRequestWrapper(request);
        }
        return request;
    }

    private boolean hasContent(HttpServletRequest request) {
        return request.getContentLength() > -1;
    }

}
