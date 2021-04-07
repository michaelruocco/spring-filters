package uk.co.mruoc.spring.filter.rewrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import uk.co.mruoc.spring.filter.logging.request.RequestWrapper;
import uk.co.mruoc.spring.filter.ResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RewriteResponseBodyFilterTest {

    private static final String REWRITTEN_BODY = "my-rewritten-body";

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final PrintWriter writer = mock(PrintWriter.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final RequestWrapper requestWrapper = mock(RequestWrapper.class);
    private final ResponseWrapper responseWrapper = mock(ResponseWrapper.class);
    private final RewriteResponseBody rewriteBody = mock(RewriteResponseBody.class);

    private final RewriteResponseBodyFilter filter = new RewriteResponseBodyFilter(
            requestWrapper,
            responseWrapper,
            rewriteBody
    );

    @BeforeEach
    void setUp() throws IOException {
        given(rewriteBody.apply(any(RewriteResponseBodyRequest.class))).willReturn(REWRITTEN_BODY);
        given(response.getWriter()).willReturn(writer);
    }

    @Test
    void shouldPassWrappedRequestToFilterChainAndRewriteBodyFunction() throws IOException, ServletException {
        HttpServletRequest wrappedRequest = mock(HttpServletRequest.class);
        given(requestWrapper.wrapIfHasContent(request)).willReturn(wrappedRequest);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(wrappedRequest, null);
        ArgumentCaptor<RewriteResponseBodyRequest> captor = ArgumentCaptor.forClass(RewriteResponseBodyRequest.class);
        verify(rewriteBody).apply(captor.capture());
        RewriteResponseBodyRequest rewriteRequest = captor.getValue();
        assertThat(rewriteRequest.getRequest()).isEqualTo(wrappedRequest);
    }

    @Test
    void shouldPassWritableResponseToFilterChainAndRewriteBodyFunction() throws IOException, ServletException {
        CapturingResponse capturingResponse = mock(CapturingResponse.class);
        given(responseWrapper.toCapturingResponse(response)).willReturn(capturingResponse);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(null, capturingResponse);
        ArgumentCaptor<RewriteResponseBodyRequest> captor = ArgumentCaptor.forClass(RewriteResponseBodyRequest.class);
        verify(rewriteBody).apply(captor.capture());
        RewriteResponseBodyRequest rewriteRequest = captor.getValue();
        assertThat(rewriteRequest.getResponse()).isEqualTo(capturingResponse);
    }

    @Test
    void shouldWriteRewrittenResponseBodyToOriginalResponse() throws IOException, ServletException {
        CapturingResponse capturingResponse = mock(CapturingResponse.class);
        given(responseWrapper.toCapturingResponse(response)).willReturn(capturingResponse);

        filter.doFilter(request, response, chain);

        InOrder inOrder = Mockito.inOrder(writer, response);
        inOrder.verify(response).setContentLength(REWRITTEN_BODY.length());
        inOrder.verify(writer).write(REWRITTEN_BODY);
        inOrder.verify(response).flushBuffer();
    }

}
