package uk.co.mruoc.spring.filter;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.enumeration.FakeEnumeration;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class RequestHeaderExtractorTest {

    private static final String NAME_1 = "headerName1";
    private static final String NAME_2 = "headerName2";

    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_3 = "value3";

    private final RequestHeaderExtractor extractor = new RequestHeaderExtractor();

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

}
