package uk.co.mruoc.spring.filter.logging.uritransform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class RegexStringTransformer implements UnaryOperator<String> {

    private final Pattern pattern;
    private final String replacement;

    public RegexStringTransformer(String regex, String replacement) {
        this(Pattern.compile(regex), replacement);
    }

    @Override
    public String apply(String value) {
        return pattern.matcher(value).replaceAll(replacement);
    }

}
