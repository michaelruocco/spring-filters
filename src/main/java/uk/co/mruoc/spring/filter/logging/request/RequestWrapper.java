package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class RequestWrapper {

    public CachedBodyHttpServletRequestWrapper wrap(HttpServletRequest request) {
        return new CachedBodyHttpServletRequestWrapper(request);
    }

}
