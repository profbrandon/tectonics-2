package util.data.algebraic;

import java.util.function.Function;

import util.counting.Cardinal;
import util.counting.Ordinal;

public class HomSum<N extends Cardinal, A> {

    private final Ordinal<N> tag;
    private final A value;

    private HomSum(final Ordinal<N> tag, final A value) {
        this.tag = tag;
        this.value = value;
    }

    public <U> U match(final Function<Ordinal<N>, Function<A, U>> matcher) {
        return matcher.apply(tag).apply(value);
    }

    public <U> U match(final HomTuple<N, Function<A, U>> matches) {
        return matches.at(tag).apply(value);
    }
}
