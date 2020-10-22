package uk.co.mruoc.spring.filter.logging;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static uk.co.mruoc.spring.filter.HeaderExtractor.extractHeaders;
import static uk.co.mruoc.spring.filter.HeaderExtractor.extractValues;

@RequiredArgsConstructor
public class HeaderMdcPopulatorFilter extends OncePerRequestFilter {

    private final Collection<String> headerNames;

    public HeaderMdcPopulatorFilter() {
        this(Arrays.asList("correlation-id", "channel-id"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        populateMdc(extractHeaders(request));
        chain.doFilter(request, response);
    }

    private void populateMdc(Map<String, Collection<String>> headers) {
        headerNames.forEach(name -> populateMdc(name, headers));
    }

    private void populateMdc(String name, Map<String, Collection<String>> headers) {
        Collection<String> values = extractValues(name, headers);
        if (values.isEmpty()) {
            return;
        }
        MDC.put(name, String.join(",", values));
    }

}
