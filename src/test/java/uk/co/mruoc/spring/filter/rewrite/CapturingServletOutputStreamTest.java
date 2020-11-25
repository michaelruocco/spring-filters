package uk.co.mruoc.spring.filter.rewrite;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CapturingServletOutputStreamTest {

    private final ByteArrayOutputStream stream = mock(ByteArrayOutputStream.class);

    private final CapturingServletOutputStream capturingStream = new CapturingServletOutputStream(stream);

    @Test
    void shouldWriteDataToStream() {
        int value = 1;

        capturingStream.write(value);

        verify(stream).write(value);
    }

    @Test
    void shouldFlushStream() throws IOException {
        capturingStream.flush();

        verify(stream).flush();
    }

    @Test
    void shouldCloseStream() throws IOException {
        capturingStream.close();

        verify(stream).close();
    }

    @Test
    void shouldNotBeReady() {
        assertThat(capturingStream.isReady()).isFalse();
    }

    @Test
    void setWriteListenerShouldDoNothing() {
        assertThatCode(() -> capturingStream.setWriteListener(null)).doesNotThrowAnyException();
    }

}
