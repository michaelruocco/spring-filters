package uk.co.mruoc.spring.filter.validation.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import uk.co.mruoc.spring.filter.HeaderAdapter;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class MandatoryHeaderValidator implements HeaderValidator {

    private final String name;

    @Override
    public Collection<String> toErrors(HeaderAdapter headers) {
        String value = headers.getAsString(name);
        if (StringUtils.hasLength(value)) {
            return Collections.singleton(toMessage(name));
        }
        return Collections.emptyList();
    }

    private static String toMessage(String name) {
        return String.format("mandatory header %s not provided", name);
    }

}
