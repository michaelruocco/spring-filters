package uk.co.mruoc.spring.filter;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.enumeration.FakeEnumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HeaderExtractorTest {

    private static final String NAME_1 = "headerName1";
    private static final String NAME_2 = "headerName2";

    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_3 = "value3";

    private final HeaderExtractor extractor = new HeaderExtractor();

    @Test
    void shouldExtractRequestHeaders() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeaderNames()).willReturn(new FakeEnumeration(NAME_1, NAME_2));
        given(request.getHeaders(NAME_1)).willReturn(new FakeEnumeration(VALUE_1, VALUE_2));
        given(request.getHeaders(NAME_2)).willReturn(new FakeEnumeration(VALUE_3));

        HeaderAdapter extracted = extractor.extractHeaders(request);

        assertThat(extracted.size()).isEqualTo(2);
        assertThat(extracted.get(NAME_1)).containsExactly(VALUE_1, VALUE_2);
        assertThat(extracted.get(NAME_2)).containsExactly(VALUE_3);
    }

    @Test
    void shouldExtractResponseHeaders() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(response.getHeaderNames()).willReturn(Arrays.asList(NAME_1, NAME_2));
        given(response.getHeaders(NAME_1)).willReturn(Arrays.asList(VALUE_1, VALUE_2));
        given(response.getHeaders(NAME_2)).willReturn(Collections.singleton(VALUE_3));

        HeaderAdapter extracted = extractor.extractHeaders(response);

        assertThat(extracted.size()).isEqualTo(2);
        assertThat(extracted.get(NAME_1)).containsExactly(VALUE_1, VALUE_2);
        assertThat(extracted.get(NAME_2)).containsExactly(VALUE_3);
    }

}
