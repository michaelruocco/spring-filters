package uk.co.mruoc.spring.filter;

import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern UUID = Pattern.compile(Regexs.UUID);

    private Patterns() {
        // utility class
    }

}
