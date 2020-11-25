package uk.co.mruoc.spring.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uk.co.mruoc.spring.filter.rewrite.CapturingResponse;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class ResponseWrapper {

    public ContentCachingResponseWrapper toCachingResponseWrapper(HttpServletResponse response) {
        return new ContentCachingResponseWrapper(response);
    }

    public CapturingResponse toCapturingResponse(HttpServletResponse response) {
        return new CapturingResponse(response);
    }

}
