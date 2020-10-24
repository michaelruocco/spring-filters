package uk.co.mruoc.spring.filter;

import java.util.regex.Pattern;

public class Patterns {

    private Patterns() {
        // utility class
    }

    public static final Pattern UUID = Pattern.compile(Regexs.UUID);

}
