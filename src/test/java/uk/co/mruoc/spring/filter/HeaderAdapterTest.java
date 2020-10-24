package uk.co.mruoc.spring.filter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class HeaderAdapterTest {

    private static final String HEADER_NAME_1 = "My-Header-1";
    private static final String HEADER_NAME_2 = "My-Header-2";

    private static final String OTHER_CASE_HEADER_NAME_1 = "mY-hEADEr-1";

    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_1_AND_2 = String.format("%s,%s", VALUE_1, VALUE_2);
    private static final String VALUE_3 = "value3";

    @Test
    void shouldReturnSize() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        int size = headers.size();

        assertThat(size).isEqualTo(2);
    }

    @Test
    void shouldReturnNamesInLowerCase() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        Collection<String> names = headers.getNames();

        assertThat(names).containsExactlyInAnyOrder(
                HEADER_NAME_1.toLowerCase(),
                HEADER_NAME_2.toLowerCase()
        );
    }

    @Test
    void shouldReturnTrueIfHeaderWithNameIsPresent() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        boolean present = headers.isPresent(HEADER_NAME_1);

        assertThat(present).isTrue();
    }

    @Test
    void shouldReturnFalseIfHeaderWithNameIsNotPresent() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        boolean present = headers.isPresent("not-present");

        assertThat(present).isFalse();
    }

    @Test
    void shouldNotMatchCaseWhenCheckingHeaderIsPresent() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        boolean present = headers.isPresent(OTHER_CASE_HEADER_NAME_1);

        assertThat(present).isTrue();
    }

    @Test
    void shouldGetValues() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        Collection<String> values = headers.get(HEADER_NAME_1);

        assertThat(values).containsExactly(VALUE_1, VALUE_2);
    }

    @Test
    void shouldNotMatchCaseWhenGettingValues() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        Collection<String> values = headers.get(OTHER_CASE_HEADER_NAME_1);

        assertThat(values).containsExactly(VALUE_1, VALUE_2);
    }

    @Test
    void shouldGetValuesAsString() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        String value = headers.getAsString(HEADER_NAME_1);

        assertThat(value).isEqualTo(VALUE_1_AND_2);
    }

    @Test
    void shouldNotMatchCaseWhenGettingValuesAsString() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        String value = headers.getAsString(OTHER_CASE_HEADER_NAME_1);

        assertThat(value).isEqualTo(VALUE_1_AND_2);
    }

    @Test
    void shouldReturnHeadersAsMap() {
        HeaderAdapter headers = new HeaderAdapter(buildValues());

        Map<String, Collection<String>> map = headers.asMap();

        assertThat(map).contains(
                entry(HEADER_NAME_1.toLowerCase(), Arrays.asList(VALUE_1, VALUE_2)),
                entry(HEADER_NAME_2.toLowerCase(), Collections.singleton(VALUE_3))
        );
    }

    private static Map<String, Collection<String>> buildValues() {
        return Map.of(
                HEADER_NAME_1, Arrays.asList(VALUE_1, VALUE_2),
                HEADER_NAME_2, Collections.singleton(VALUE_3)
        );
    }

}
