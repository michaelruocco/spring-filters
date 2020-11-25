package uk.co.mruoc.spring.filter.logging.request;


import javax.servlet.http.HttpServletRequest;

public interface RequestBodyExtractor {

    String extractBody(HttpServletRequest request);

}
