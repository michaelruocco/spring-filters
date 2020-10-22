package uk.co.mruoc.spring.filter.validation.validator;

import java.util.Collection;
import java.util.Map;

public interface HeaderValidator {

    Collection<String> toErrors(Map<String, Collection<String>> headers);

}
