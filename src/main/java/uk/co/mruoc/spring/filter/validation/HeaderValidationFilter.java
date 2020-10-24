package uk.co.mruoc.spring.filter.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.HeaderExtractor;
import uk.co.mruoc.spring.filter.validation.validator.HeaderValidator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class HeaderValidationFilter extends OncePerRequestFilter {

    private final HeaderValidator validator;
    private final HandlerExceptionResolver resolver;
    private final HeaderExtractor extractor;

    public HeaderValidationFilter(HeaderValidator validator, HandlerExceptionResolver resolver) {
        this(validator, resolver, new HeaderExtractor());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            HeaderAdapter headers = extractor.extractHeaders(request);
            validator.validate(headers);
            chain.doFilter(request, response);
        } catch (InvalidHeaderException e) {
            resolver.resolveException(request, response, null, e);
        }
    }

}
