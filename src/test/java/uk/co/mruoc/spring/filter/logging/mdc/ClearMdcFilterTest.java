package uk.co.mruoc.spring.filter.logging.mdc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Slf4j
class ClearMdcFilterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final Filter filter = new ClearMdcFilter();

    @BeforeEach
    @AfterEach
    public void clearMdc() {
        MDC.clear();
    }

    @Test
    void shouldClearMdc() throws Exception {
        MDC.put("value-1", "my-value-1");
        MDC.put("value-2", "my-value-2");

        filter.doFilter(request, response, chain);

        assertThat(MDC.getCopyOfContextMap()).isNull();
    }

}
