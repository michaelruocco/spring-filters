package uk.co.mruoc.spring.filter.logging.header;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.HeaderExtractor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.co.mruoc.logging.LogOutputGenerator.generateLogOutput;

@Slf4j
class HeaderMdcPopulatorFilterTest {

    private static final String NAME_1 = "My-Header-1";
    private static final String NAME_2 = "My-Header-2";
    private static final String VALUE_1 = "my-value-1";

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final Collection<String> names = Arrays.asList(NAME_1, NAME_2);
    private final HeaderExtractor extractor = mock(HeaderExtractor.class);

    private final Filter filter = new HeaderMdcPopulatorFilter(names, extractor);

    @BeforeEach
    public void setUp() {
        MDC.clear();
    }

    @AfterEach
    public void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldNotPopulateHeadersInMdcContextIfNotPresent() throws Exception {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(extractor.extractHeaders(request)).willReturn(headers);

        filter.doFilter(request, response, chain);

        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace("INFO [::::::] test log message");
        assertThat(MDC.getCopyOfContextMap()).isNull();
    }

    @Test
    void shouldPopulateHeaderInMdcContextIfPresent() throws Exception {
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headers.getAsString(NAME_1)).willReturn(VALUE_1);
        given(extractor.extractHeaders(request)).willReturn(headers);

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [%s::::::] test log message", VALUE_1);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).containsExactly(entry(NAME_1.toLowerCase(), VALUE_1));
    }

    @Test
    void shouldPopulateMultipleHeadersInMdcContextIfPresent() throws Exception {
        String value2 = "value2";
        HeaderAdapter headers = mock(HeaderAdapter.class);
        given(headers.getAsString(NAME_1)).willReturn(VALUE_1);
        given(headers.getAsString(NAME_2)).willReturn(value2);
        given(extractor.extractHeaders(request)).willReturn(headers);

        filter.doFilter(request, response, chain);

        String expectedOutput = String.format("INFO [%s:%s:::::] test log message", VALUE_1, value2);
        assertThat(generateLogOutput()).isEqualToIgnoringWhitespace(expectedOutput);
        assertThat(MDC.getCopyOfContextMap()).containsExactly(
                entry(NAME_1.toLowerCase(), VALUE_1),
                entry(NAME_2.toLowerCase(), value2)
        );
    }

}
