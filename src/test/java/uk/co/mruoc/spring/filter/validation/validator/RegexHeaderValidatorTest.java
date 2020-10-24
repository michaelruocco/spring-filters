package uk.co.mruoc.spring.filter.validation.validator;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RegexHeaderValidatorTest {

    private static final String NAME = "my-header";
    private static final String REGEX = "\\d{1,2}";

    private final HeaderValidator validator = new RegexHeaderValidator(NAME, REGEX);

    @Test
    void shouldNotReturnErrorIfHeaderIsNotPresent() {
        HeaderAdapter headers = new HeaderAdapter(Map.of());

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).isEmpty();
    }

    @Test
    void shouldReturnErrorIfHeaderIsPresentButValueIsEmpty() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singleton("")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).containsExactly("header my-header value  does not match regex \\d{1,2}");
    }

    @Test
    void shouldReturnErrorIfHeaderValueDoesNotMatchRegex() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singleton("1234")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).containsExactly("header my-header value 1234 does not match regex \\d{1,2}");
    }

    @Test
    void shouldNotReturnErrorIfHeaderIsPresentAndMatchesRegex() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singleton("12")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).isEmpty();
    }

}
