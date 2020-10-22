package uk.co.mruoc.spring.filter.validation.validator;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.spring.filter.validation.InvalidHeaderException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompositeHeaderValidator {

    private final Collection<HeaderValidator> validators;

    public CompositeHeaderValidator() {
        this(Arrays.asList(
                new ChannelIdHeaderValidator(),
                new CorrelationIdHeaderValidator()
        ));
    }

    public void validate(HttpServletRequest request) {
        validate(extractHeaders(request));
    }

    private Map<String, Collection<String>> extractHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(request.getHeaders(header))));
    }

    private void validate(Map<String, Collection<String>> headers) {
        Collection<String> messages = validators.stream()
                .map(validator -> validator.toErrors(headers))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (!messages.isEmpty()) {
            throw new InvalidHeaderException(String.join(", ", messages));
        }
    }

}
