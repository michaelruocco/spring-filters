package uk.co.mruoc.spring.filter.validation.validator;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegexHeaderValidator implements HeaderValidator {

    private final String name;
    private final Pattern pattern;

    public RegexHeaderValidator(String name, String pattern) {
        this(name, Pattern.compile(pattern));
    }

    @Override
    public Collection<String> toErrors(HeaderAdapter headers) {
        Collection<String> values = headers.get(name);
        return toErrors(values);
    }

    private Collection<String> toErrors(Collection<String> values) {
        return values.stream()
                .map(this::validate)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<String> validate(String value) {
        if (pattern.matcher(value).matches()) {
            return Optional.empty();
        }
        return Optional.of(toMessage(value));
    }

    private String toMessage(String value) {
        return String.format("header %s value %s does not match regex %s", name, value, pattern.pattern());
    }

}
