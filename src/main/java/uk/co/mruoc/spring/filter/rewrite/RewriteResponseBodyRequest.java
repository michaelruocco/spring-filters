package uk.co.mruoc.spring.filter.rewrite;

import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;

@Builder
@Data
public class RewriteResponseBodyRequest {

    private final HttpServletRequest request;
    private final CapturingResponse response;

    public boolean isPostRequest() {
        return request.getMethod().equals("POST");
    }

    public String getResponseBody() {
        try {
            return response.getBodyAsString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean has2xxStatus() {
        int status = response.getStatus();
        return status >= 200 && status <= 299;
    }

}
