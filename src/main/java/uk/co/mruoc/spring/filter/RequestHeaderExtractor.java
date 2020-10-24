package uk.co.mruoc.spring.filter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequestHeaderExtractor {

    public HeaderAdapter extractHeaders(HttpServletRequest request) {
        Map<String, Collection<String>> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));
        return new HeaderAdapter(headers);
    }

}
