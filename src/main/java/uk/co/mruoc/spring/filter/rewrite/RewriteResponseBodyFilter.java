package uk.co.mruoc.spring.filter.rewrite;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.spring.filter.logging.request.RequestWrapper;
import uk.co.mruoc.spring.filter.ResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RewriteResponseBodyFilter implements Filter {

    private final RequestWrapper requestWrapper;
    private final ResponseWrapper responseWrapper;
    private final RewriteResponseBody rewriteBody;

    public RewriteResponseBodyFilter(RewriteResponseBody rewriteBody) {
        this(new RequestWrapper(), new ResponseWrapper(), rewriteBody);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest cachingRequest = requestWrapper.wrapIfHasContent((HttpServletRequest) request);
        CapturingResponse capturingResponse = responseWrapper.toCapturingResponse((HttpServletResponse) response);

        chain.doFilter(cachingRequest, capturingResponse);

        RewriteResponseBodyRequest rewriteBodyRequest = RewriteResponseBodyRequest.builder()
                .request(cachingRequest)
                .response(capturingResponse)
                .build();
        String body = rewriteBody.apply(rewriteBodyRequest);
        response.setContentLength(body.length());
        response.getWriter().write(body);
        response.flushBuffer();
    }

}
