package uk.co.mruoc.spring.filter.validation.validator;

import uk.co.mruoc.spring.filter.Patterns;

public class CorrelationIdHeaderValidator extends CompositeHeaderValidator {

    public CorrelationIdHeaderValidator() {
        this("correlation-id");
    }

    public CorrelationIdHeaderValidator(String headerName) {
        super(
                new MandatoryHeaderValidator(headerName),
                new RegexHeaderValidator(headerName, Patterns.UUID)
        );
    }

}
