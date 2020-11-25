package uk.co.mruoc.spring.filter.rewrite;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RewriteResponseBodyRequestTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final CapturingResponse response = mock(CapturingResponse.class);

    private final RewriteResponseBodyRequest rewriteRequest = RewriteResponseBodyRequest.builder()
            .request(request)
            .response(response)
            .build();

    @Test
    void shouldReturnRequest() {
        assertThat(rewriteRequest.getRequest()).isEqualTo(request);
    }

    @Test
    void shouldReturnResponse() {
        assertThat(rewriteRequest.getResponse()).isEqualTo(response);
    }

    @Test
    void shouldReturnTrueIfMethodIsPost() {
        given(request.getMethod()).willReturn("POST");

        boolean postRequest = rewriteRequest.isPostRequest();

        assertThat(postRequest).isTrue();
    }

    @Test
    void shouldReturnFalseIfMethodIsNotPost() {
        given(request.getMethod()).willReturn("PUT");

        boolean postRequest = rewriteRequest.isPostRequest();

        assertThat(postRequest).isFalse();
    }

    @Test
    void shouldReturnResponseBody() throws IOException {
        String expectedBody = "expectedBody";
        given(response.getBodyAsString()).willReturn(expectedBody);

        String body = rewriteRequest.getResponseBody();

        assertThat(body).isEqualTo(expectedBody);
    }

    @Test
    void shouldThrowExceptionIfGetBodyAsStringThrowsException() throws IOException {
        given(response.getBodyAsString()).willThrow(IOException.class);

        Throwable error = catchThrowable(rewriteRequest::getResponseBody);

        assertThat(error)
                .isInstanceOf(UncheckedIOException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void shouldReturnTrueIfResponseStatusIsIn2xxRange() {
        assertThat(toRewriteRequest(givenResponseStatus(199)).has2xxStatus()).isFalse();

        assertThat(toRewriteRequest(givenResponseStatus(200)).has2xxStatus()).isTrue();
        assertThat(toRewriteRequest(givenResponseStatus(299)).has2xxStatus()).isTrue();

        assertThat(toRewriteRequest(givenResponseStatus(300)).has2xxStatus()).isFalse();
    }

    private static RewriteResponseBodyRequest toRewriteRequest(CapturingResponse capturingResponse) {
        return RewriteResponseBodyRequest.builder()
                .response(capturingResponse)
                .build();
    }

    private CapturingResponse givenResponseStatus(int status) {
        CapturingResponse responseWithStatus = mock(CapturingResponse.class);
        given(responseWithStatus.getStatus()).willReturn(status);
        return responseWithStatus;
    }

}
