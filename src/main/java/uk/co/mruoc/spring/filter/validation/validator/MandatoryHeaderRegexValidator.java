package uk.co.mruoc.spring.filter.validation.validator;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static uk.co.mruoc.spring.filter.HeaderExtractor.extractValues;

@RequiredArgsConstructor
@Data
public class MandatoryHeaderRegexValidator implements HeaderValidator {

    private final String name;
    private final Pattern pattern;

    public MandatoryHeaderRegexValidator(String name, String pattern) {
        this(name, Pattern.compile(pattern));
    }

    @Override
    public Collection<String> toErrors(Map<String, Collection<String>> headers) {
        Collection<String> values = extractValues(name, headers);
        if (values.isEmpty()) {
            return Collections.singleton(String.format("mandatory header %s not provided", name));
        }
        return toErrors(values);
    }

    public Collection<String> toErrors(Collection<String> values) {
        return values.stream()
                .map(this::validate)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public Optional<String> validate(String value) {
        if (pattern.matcher(value).matches()) {
            return Optional.empty();
        }
        return Optional.of(toInvalidValueMessage(value));
    }

    private String toInvalidValueMessage(String value) {
        return String.format("header %s value %s does not match regex %s", name, value, pattern.pattern());
    }

}
