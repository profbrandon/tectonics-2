package util.data.algebraic;

import java.util.function.Function;

import util.counting.Ordinal;
import util.counting.OrdinalSet;

public class NSum<O extends Ordinal, A> {

    private final OrdinalSet<O> tag;
    private final A value;

    private NSum(final OrdinalSet<O> tag, final A value) {
        this.tag = tag;
        this.value = value;
    }

    public <U> U match(final Function<OrdinalSet<O>, Function<A, U>> matcher) {
        return matcher.apply(tag).apply(value);
    }
}
