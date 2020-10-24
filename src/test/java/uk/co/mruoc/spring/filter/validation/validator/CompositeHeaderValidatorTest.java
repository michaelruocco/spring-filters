package uk.co.mruoc.spring.filter.validation.validator;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.validation.InvalidHeaderException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CompositeHeaderValidatorTest {

    private final HeaderValidator validator1 = mock(HeaderValidator.class);
    private final HeaderValidator validator2 = mock(HeaderValidator.class);
    private final Collection<HeaderValidator> validators = Arrays.asList(validator1, validator2);

    private final CompositeHeaderValidator validator = new CompositeHeaderValidator(validators);

    @Test
    void shouldValidateHeadersWithEachValidator() {
        HeaderAdapter headers = buildHeaders();

        validator.validate(headers);

        ArgumentCaptor<HeaderAdapter> captor = ArgumentCaptor.forClass(HeaderAdapter.class);
        verify(validator1).toErrors(captor.capture());
        verify(validator2).toErrors(captor.capture());
        assertThat(captor.getAllValues()).containsExactly(headers, headers);
    }

    @Test
    void shouldNotThrowExceptionIfNoValidatorsReturnErrors() {
        HeaderAdapter headers = buildHeaders();

        assertThatCode(() -> validator.validate(headers)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWithErrorMessagesReturnedFromValidators() {
        HeaderAdapter headers = buildHeaders();
        given(validator1.toErrors(headers)).willReturn(Collections.singleton("error1"));
        given(validator2.toErrors(headers)).willReturn(Collections.singleton("error2"));

        Throwable error = catchThrowable(() -> validator.validate(headers));

        assertThat(error)
                .isInstanceOf(InvalidHeaderException.class)
                .hasMessage("error1, error2");
    }

    private HeaderAdapter buildHeaders() {
        return new HeaderAdapter(Map.of("header1", Collections.singleton("value1")));
    }

}
