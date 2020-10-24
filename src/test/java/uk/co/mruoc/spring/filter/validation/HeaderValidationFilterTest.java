package uk.co.mruoc.spring.filter.validation;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.HeaderExtractor;
import uk.co.mruoc.spring.filter.validation.validator.CompositeHeaderValidator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HeaderValidationFilterTest {

    private final CompositeHeaderValidator validator = mock(CompositeHeaderValidator.class);
    private final HandlerExceptionResolver resolver = mock(HandlerExceptionResolver.class);
    private final HeaderExtractor extractor = mock(HeaderExtractor.class);

    private final Filter filter = new HeaderValidationFilter(validator, resolver, extractor);

    @Test
    void shouldValidateRequestBeforeCompletingFilterChain() throws IOException, ServletException {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        HttpServletRequest request = givenRequestWithHeaders(headers);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        InOrder verifier = inOrder(validator, filterChain);
        verifier.verify(validator).validate(headers);
        verifier.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldPassExceptionsToResolver() throws IOException, ServletException {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        HttpServletRequest request = givenRequestWithHeaders(headers);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        Exception expectedException = new InvalidHeaderException("error");
        doThrow(expectedException).when(validator).validate(headers);

        filter.doFilter(request, response, chain);

        verify(resolver).resolveException(request, response, null, expectedException);
    }

    private HttpServletRequest givenRequestWithHeaders(HeaderAdapter headers) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(extractor.extractHeaders(request)).willReturn(headers);
        return request;
    }

}
