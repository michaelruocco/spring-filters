package uk.co.mruoc.spring.filter.rewrite;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CapturingResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture;

    private ServletOutputStream output;
    private PrintWriter writer;

    public CapturingResponse(HttpServletResponse response) {
        this(response, new ByteArrayOutputStream());
    }

    public CapturingResponse(HttpServletResponse response, ByteArrayOutputStream capture) {
        super(response);
        this.capture = capture;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response");
        }
        if (output == null) {
            output = new CapturingServletOutputStream(capture);
        }
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (output != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response");
        }
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(capture, getCharacterEncoding()));
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (output != null) {
            output.flush();
        }
    }

    public byte[] getBodyAsBytes() throws IOException {
        if (writer != null) {
            writer.close();
        } else if (output != null) {
            output.close();
        }
        return capture.toByteArray();
    }

    public String getBodyAsString() throws IOException {
        return new String(getBodyAsBytes(), getCharacterEncoding());
    }

}
