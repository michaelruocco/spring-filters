package uk.co.mruoc.spring.filter.validation.validator;

import uk.co.mruoc.spring.filter.HeaderAdapter;
import uk.co.mruoc.spring.filter.validation.InvalidHeaderException;

import java.util.Collection;

public interface HeaderValidator {

    default void validate(HeaderAdapter headers) {
        Collection<String> errors = toErrors(headers);
        if (!errors.isEmpty()) {
            throw new InvalidHeaderException(String.join(", ", errors));
        }
    }

    Collection<String> toErrors(HeaderAdapter headers);

}
