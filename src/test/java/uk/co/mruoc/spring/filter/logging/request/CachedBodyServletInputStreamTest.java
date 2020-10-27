package uk.co.mruoc.spring.filter.logging.request;

import org.junit.jupiter.api.Test;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CachedBodyServletInputStreamTest {

    private final ServletInputStream stream = new CachedBodyServletInputStream("my-body".getBytes());

    @Test
    void shouldNotBeFinished() {
        assertThat(stream.isFinished()).isFalse();
    }

    @Test
    void shouldBeReady() {
        assertThat(stream.isReady()).isTrue();
    }

    @Test
    void shouldRead() throws IOException {
        assertThat(stream.read()).isEqualTo(109);
    }

    @Test
    void shouldThrowExceptionIfSetReadListener() {
        ReadListener listener = mock(ReadListener.class);

        Throwable error = catchThrowable(() -> stream.setReadListener(listener));

        assertThat(error).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnFinishedTrueIfStreamAvailableIsZero() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        given(inputStream.available()).willReturn(0);
        ServletInputStream cachedStream = new CachedBodyServletInputStream(inputStream);

        boolean finished = cachedStream.isFinished();

        assertThat(finished).isTrue();
    }

    @Test
    void shouldReturnFinishedFalseIfStreamAvailableIsGreaterThanZero() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        given(inputStream.available()).willReturn(1);
        ServletInputStream cachedStream = new CachedBodyServletInputStream(inputStream);

        boolean finished = cachedStream.isFinished();

        assertThat(finished).isFalse();
    }

    @Test
    void shouldReturnFinishedFalseIfStreamAvailableThrowsException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        IOException expectedCause = new IOException("error-cause");
        given(inputStream.available()).willThrow(expectedCause);
        ServletInputStream cachedStream = new CachedBodyServletInputStream(inputStream);

        boolean finished = cachedStream.isFinished();

        assertThat(finished).isFalse();
    }

}
