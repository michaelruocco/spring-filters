package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class CachedBodyServletInputStream extends ServletInputStream {

    private final InputStream inputStream;

    public CachedBodyServletInputStream(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
    }

    @Override
    public boolean isFinished() {
        try {
            return inputStream.available() == 0;
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

}
