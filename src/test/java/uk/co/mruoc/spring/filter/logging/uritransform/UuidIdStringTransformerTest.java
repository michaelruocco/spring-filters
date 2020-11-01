package uk.co.mruoc.spring.filter.logging.uritransform;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UuidIdStringTransformerTest {

    @Test
    void shouldReplaceRegexMatchesWithReplacementValue() {
        RegexStringTransformer replacer = new UuidIdStringTransformer();
        String value = "/my-resources/dede96a0-9b59-433c-88be-500535dd2562";

        String replaced = replacer.apply(value);

        assertThat(replaced).isEqualTo("/my-resources/{id}");
    }

    @Test
    void shouldReplaceMultipleRegexMatchesWithReplacementValue() {
        RegexStringTransformer replacer = new UuidIdStringTransformer();
        String value = "/my-resources/6f737758-e958-4e7d-8873-2e4e72797296/" +
                "my-sub-resources/906e93dc-029d-4d8d-874f-d6f63ab47fe1";

        String replaced = replacer.apply(value);

        assertThat(replaced).isEqualTo("/my-resources/{id}/my-sub-resources/{id}");
    }

}
