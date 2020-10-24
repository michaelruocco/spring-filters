package uk.co.mruoc.spring.filter.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class TransformRequestUriMdcPopulatorFilter extends OncePerRequestFilter {

    private static final String DEFAULT_NAME = "transformed-request-uri";

    private final String name;
    private final Function<String, String> transformer;

    public TransformRequestUriMdcPopulatorFilter(UnaryOperator<String> transformer) {
        this(DEFAULT_NAME, transformer);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        populateRequestUri(request);
        chain.doFilter(request, response);
    }

    private void populateRequestUri(HttpServletRequest request) {
        MDC.put(name, transform(request.getRequestURI()));
    }

    private String transform(String uri) {
        return transformer.apply(uri);
    }

    @SafeVarargs
    public static UnaryOperator<String> compose(Function<String, String>... functions) {
        return compose(Arrays.asList(functions));
    }

    public static UnaryOperator<String> compose(Collection<Function<String, String>> functions) {
        return compose(functions.stream());
    }

    public static UnaryOperator<String> compose(Stream<Function<String, String>> functions) {
        return functions.reduce(Function.identity(), Function::compose)::apply;
    }

}
