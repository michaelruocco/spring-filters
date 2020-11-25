package uk.co.mruoc.spring.filter.logging.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@RequiredArgsConstructor
public class SimpleRequestBodyExtractor implements RequestBodyExtractor {

    @Override
    public String extractBody(HttpServletRequest request) {
        try {
            return IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
