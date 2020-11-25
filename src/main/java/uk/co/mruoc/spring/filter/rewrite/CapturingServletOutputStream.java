package uk.co.mruoc.spring.filter.rewrite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
public class CapturingServletOutputStream extends ServletOutputStream {

    @Getter
    private final ByteArrayOutputStream outputStream;

    @Override
    public void write(int b) {
        outputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        // intentionally blank
    }

}
