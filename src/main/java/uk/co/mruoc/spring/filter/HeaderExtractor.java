package uk.co.mruoc.spring.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HeaderExtractor {

    public HeaderAdapter extractHeaders(HttpServletRequest request) {
        Map<String, Collection<String>> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));
        return new HeaderAdapter(headers);
    }

    public HeaderAdapter extractHeaders(HttpServletResponse response) {
        Map<String, Collection<String>> headers = response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(Function.identity(), response::getHeaders));
        return new HeaderAdapter(headers);
    }

}
