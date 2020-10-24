package uk.co.mruoc.spring.filter.validation.validator;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MandatoryHeaderValidatorTest {

    private static final String NAME = "my-header";

    private final HeaderValidator validator = new MandatoryHeaderValidator(NAME);

    @Test
    void shouldReturnErrorIfHeaderIsNotPresent() {
        HeaderAdapter headers = new HeaderAdapter(Map.of());

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).containsExactly("mandatory header my-header not provided");
    }

    @Test
    void shouldReturnErrorIfHeaderIsPresentByHasEmptyValue() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singletonList("")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).containsExactly("mandatory header my-header not provided");
    }

    @Test
    void shouldNotReturnErrorIfHeaderIsPresentAndHasValue() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singletonList("my-value")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).isEmpty();
    }

}
