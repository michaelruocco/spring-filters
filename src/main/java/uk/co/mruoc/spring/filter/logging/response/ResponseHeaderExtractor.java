package uk.co.mruoc.spring.filter.logging.response;

import uk.co.mruoc.spring.filter.HeaderAdapter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResponseHeaderExtractor {

    public HeaderAdapter extractHeaders(HttpServletResponse response) {
        Map<String, Collection<String>> headers = response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(Function.identity(), response::getHeaders, this::mergeValues));
        return new HeaderAdapter(headers);
    }

    private Collection<String> mergeValues(Collection<String> values1, Collection<String> values2) {
        Set<String> mergedValues = new HashSet<>(values1);
        mergedValues.addAll(values2);
        return mergedValues;
    }

}
