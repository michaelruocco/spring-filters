package uk.co.mruoc.spring.filter.validation.validator;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.Regexs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdHeaderValidatorTest {

    private static final String NAME = "correlation-id";

    private final HeaderValidator validator = new CorrelationIdHeaderValidator();

    @Test
    void shouldReturnErrorIfCorrelationIdNotPresent() {
        HeaderAdapter headers = new HeaderAdapter(Map.of());

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).contains("mandatory header correlation-id not provided");
    }

    @Test
    void shouldReturnErrorIfCorrelationIdNotAValidUUID() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singleton("123")));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).contains(String.format("header %s value 123 does not match regex %s", NAME, Regexs.UUID));
    }

    @Test
    void shouldNotReturnErrorIfCorrelationIdIsPresentAndHasValidUUIDValue() {
        HeaderAdapter headers = new HeaderAdapter(Map.of(NAME, Collections.singleton(UUID.randomUUID().toString())));

        Collection<String> errors = validator.toErrors(headers);

        assertThat(errors).isEmpty();
    }

}
