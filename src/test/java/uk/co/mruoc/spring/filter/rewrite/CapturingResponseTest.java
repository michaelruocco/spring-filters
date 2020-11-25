package uk.co.mruoc.spring.filter.rewrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CapturingResponseTest {

    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final ByteArrayOutputStream body = new ByteArrayOutputStream();

    private final CapturingResponse capturingResponse = new CapturingResponse(response, body);

    @BeforeEach
    void setUp() {
        given(response.getCharacterEncoding()).willReturn(Charset.defaultCharset().name());
    }

    @Test
    void shouldReturnRewriteResponseServletOutputStreamWithBody() {
        ServletOutputStream outputStream = capturingResponse.getOutputStream();

        assertThat(outputStream).isInstanceOf(CapturingServletOutputStream.class);
        CapturingServletOutputStream stream = (CapturingServletOutputStream) outputStream;
        assertThat(stream.getOutputStream()).isEqualTo(body);
    }

    @Test
    void shouldThrowExceptionIfGetOutputStreamCalledAfterGetWriter() throws IOException {
        capturingResponse.getWriter();

        Throwable error = catchThrowable(capturingResponse::getOutputStream);

        assertThat(error)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("getWriter() has already been called on this response");
    }

    @Test
    void shouldReturnPrintWriterThatWritesToBody() throws IOException {
        PrintWriter writer = capturingResponse.getWriter();
        String testValue = "another-test";
        writer.write(testValue);
        writer.flush();

        assertThat(new String(body.toByteArray())).isEqualTo(testValue);
    }

    @Test
    void shouldThrowExceptionIfGetWriterCalledAfterGetOutputStream() {
        capturingResponse.getOutputStream();

        Throwable error = catchThrowable(capturingResponse::getWriter);

        assertThat(error)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("getOutputStream() has already been called on this response");
    }

    @Test
    void shouldReturnBodyAsString() throws IOException {
        String expectedBody = "test-body";
        body.writeBytes(expectedBody.getBytes());

        String body = capturingResponse.getBodyAsString();

        assertThat(body).isEqualTo(expectedBody);
    }

}
