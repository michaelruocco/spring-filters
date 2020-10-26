package uk.co.mruoc.spring.filter.logging.request;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface RequestBodyExtractor {

    String extractBody(HttpServletRequest request) throws IOException;

}
