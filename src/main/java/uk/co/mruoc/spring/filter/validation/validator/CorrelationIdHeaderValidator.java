package uk.co.mruoc.spring.filter.validation.validator;

import java.util.regex.Pattern;

public class CorrelationIdHeaderValidator extends MandatoryHeaderRegexValidator {

    private static final String REGEX = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    public CorrelationIdHeaderValidator() {
        super("correlation-id", Pattern.compile(REGEX));
    }

}
