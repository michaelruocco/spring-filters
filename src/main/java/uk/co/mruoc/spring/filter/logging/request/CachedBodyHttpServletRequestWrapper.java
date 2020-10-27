package uk.co.mruoc.spring.filter.logging.request;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

public class CachedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bytes;

    public CachedBodyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.bytes = toByteArray(request);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(bytes);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    }

    private byte[] toByteArray(HttpServletRequest request) {
        try {
            return IOUtils.toByteArray(request.getInputStream(), request.getContentLength());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
