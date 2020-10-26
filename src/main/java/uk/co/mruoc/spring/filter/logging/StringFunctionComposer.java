package uk.co.mruoc.spring.filter.logging;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface StringFunctionComposer {

    @SafeVarargs
    static UnaryOperator<String> compose(Function<String, String>... functions) {
        return compose(Arrays.asList(functions));
    }

    static UnaryOperator<String> compose(Collection<Function<String, String>> functions) {
        return compose(functions.stream());
    }

    static UnaryOperator<String> compose(Stream<Function<String, String>> functions) {
        return functions.reduce(Function.identity(), Function::compose)::apply;
    }

}
