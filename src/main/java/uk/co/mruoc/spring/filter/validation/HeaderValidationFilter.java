package uk.co.mruoc.spring.filter.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import uk.co.mruoc.spring.filter.validation.validator.ChannelIdHeaderValidator;
import uk.co.mruoc.spring.filter.validation.validator.CorrelationIdHeaderValidator;
import uk.co.mruoc.spring.filter.validation.validator.HeaderValidator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HeaderValidationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;
    private final Collection<HeaderValidator> validators;

    public HeaderValidationFilter(HandlerExceptionResolver resolver) {
        this(resolver, Arrays.asList(
                new ChannelIdHeaderValidator(),
                new CorrelationIdHeaderValidator()
        ));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            validate(extractHeaders(request));
            chain.doFilter(request, response);
        } catch (InvalidHeaderException e) {
            resolver.resolveException(request, response, null, e);
        }
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
