package uk.co.mruoc.spring.filter.logging.response;

import uk.co.mruoc.spring.filter.HeaderAdapter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResponseHeaderExtractor {

    public HeaderAdapter extractHeaders(HttpServletResponse response) {
        Map<String, Collection<String>> headers = response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(Function.identity(), response::getHeaders));
        return new HeaderAdapter(headers);
    }

}
