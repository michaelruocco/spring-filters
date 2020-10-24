package uk.co.mruoc.spring.filter.logging;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.spring.filter.Patterns;

import static org.assertj.core.api.Assertions.assertThat;

class RegexStringTransformerTest {

    @Test
    void shouldReplaceRegexMatchesWithReplacementValue() {
        RegexStringTransformer replacer = new RegexStringTransformer(Patterns.UUID, "{id}");
        String value = "/my-resources/dede96a0-9b59-433c-88be-500535dd2562";

        String replaced = replacer.apply(value);

        assertThat(replaced).isEqualTo("/my-resources/{id}");
    }

    @Test
    void shouldReplaceMultipleRegexMatchesWithReplacementValue() {
        RegexStringTransformer replacer = new RegexStringTransformer("\\d{10}", "{key}");
        String value = "/my-resources/1234567890/my-sub-resources/0987654321";

        String replaced = replacer.apply(value);

        assertThat(replaced).isEqualTo("/my-resources/{key}/my-sub-resources/{key}");
    }

}
