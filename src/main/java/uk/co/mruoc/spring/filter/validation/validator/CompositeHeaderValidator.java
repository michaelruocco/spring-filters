package uk.co.mruoc.spring.filter.validation.validator;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import java.util.Arrays;
import java.util.Collection;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CompositeHeaderValidator implements HeaderValidator {

    private final Collection<HeaderValidator> validators;

    public CompositeHeaderValidator(HeaderValidator... validators) {
        this(Arrays.asList(validators));
    }

    @Override
    public Collection<String> toErrors(HeaderAdapter headers) {
        return validators.stream()
                .map(validator -> validator.toErrors(headers))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
