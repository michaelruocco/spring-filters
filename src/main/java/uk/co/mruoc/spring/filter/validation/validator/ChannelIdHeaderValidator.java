package uk.co.mruoc.spring.filter.validation.validator;


import java.util.regex.Pattern;

public class ChannelIdHeaderValidator extends MandatoryHeaderRegexValidator {

    private static final String REGEX = "^.{3,30}$";

    public ChannelIdHeaderValidator() {
        super("channel-id", Pattern.compile(REGEX));
    }

}
