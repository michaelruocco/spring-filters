package uk.co.mruoc.spring.filter.logging.uritransform;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.co.mruoc.logging.LogOutputUtils.generateLogOutput;
import static uk.co.mruoc.spring.filter.logging.uritransform.TransformRequestUriMdcPopulatorFilter.compose;

class TransformRequestUriMdcPopulatorFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final Filter filter = new TransformRequestUriMdcPopulatorFilter(compose(
            new AppendTransform(),
            new PrependTransform()
    ));

    @BeforeEach
    public void setUp() {
        MDC.clear();
    }

    @AfterEach
    public void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldPopulateTransformedRequestUriMdcContext() throws Exception {
        String uri = "/my-resources";
        given(request.getRequestURI()).willReturn(uri);

        filter.doFilter(request, response, chain);

        String transformedUri = String.format("/prepended%s/appended", uri);
        String expectedOutput = String.format("INFO [::::::%s] test log message", transformedUri);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).contains(entry("transformed-request-uri", transformedUri));
    }

    private static class PrependTransform implements Function<String, String> {

        @Override
        public String apply(String s) {
            return "/prepended" + s;
        }

    }

    private static class AppendTransform implements Function<String, String> {

        @Override
        public String apply(String s) {
            return s + "/appended";
        }

    }

}
