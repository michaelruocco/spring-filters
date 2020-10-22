package uk.co.mruoc.spring.filter;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface HeaderExtractor {

    static Map<String, Collection<String>> extractHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));
    }

    static Collection<String> extractValues(String name, Map<String, Collection<String>> headers) {
        return headers.keySet().stream()
                .filter(key -> key.equalsIgnoreCase(name))
                .map(headers::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
