package uk.co.mruoc.spring.filter.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidHeaderExceptionTest {

    @Test
    void shouldReturnMessage() {
        String message = "my-message";

        Throwable error = new InvalidHeaderException(message);

        assertThat(error.getMessage()).isEqualTo(message);
    }

}
