package uk.co.mruoc.spring.filter.logging.mdc;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.RequestHeaderExtractor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class HeaderMdcPopulatorFilter extends OncePerRequestFilter {

    private final Collection<String> names;
    private final RequestHeaderExtractor extractor;

    public HeaderMdcPopulatorFilter(String... names) {
        this(Arrays.asList(names), new RequestHeaderExtractor());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        populateMdc(extractor.extractHeaders(request));
        chain.doFilter(request, response);
    }

    private void populateMdc(HeaderAdapter headers) {
        names.forEach(name -> populateMdcIfPresent(name, headers));
    }

    private void populateMdcIfPresent(String name, HeaderAdapter headers) {
        String value = headers.getAsString(name);
        if (StringUtils.hasLength(value)) {
            return;
        }
        MDC.put(name.toLowerCase(), value);
    }

}
