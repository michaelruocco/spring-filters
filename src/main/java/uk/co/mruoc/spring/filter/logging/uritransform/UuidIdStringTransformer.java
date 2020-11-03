package uk.co.mruoc.spring.filter.logging.uritransform;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.spring.filter.Patterns;

@Slf4j
public class UuidIdStringTransformer extends RegexStringTransformer{

    private static final String DEFAULT_REPLACEMENT = "{id}";

    public UuidIdStringTransformer() {
        this(DEFAULT_REPLACEMENT);
    }

    public UuidIdStringTransformer(String replacement) {
        super(Patterns.UUID, replacement);
    }

}
