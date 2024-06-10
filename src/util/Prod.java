package util;

import java.util.function.Function;

public class Prod<A,B> {

    private final A first;
    private final B second;

    private Prod(final A first, final B second) {
        assert first != null;
        assert second != null;

        this.first = first;
        this.second = second;
    }

    public final <U> U destroy(final Function<A, Function<B, U>> curried) {
        return curried.apply(first).apply(second);
    }

    public final A first() {
        return first; // this.destroy(a -> { return (b -> a); });
    }

    public final B second() {
        return second; // this.destroy(a -> { return (b -> b); });
    }

    public final boolean equals(final Prod<A,B> pair) {
        return this.first.equals(pair.first) && this.second.equals(pair.second);
    }

    public static <A,B> Prod<A,B> pair(final A first, final B second) {
        return new Prod<A,B>(first, second);
    }

    public static <A,B,U> U destroy(final Prod<A,B> pair, final Function<A, Function<B, U>> curried) {
        return pair.destroy(curried);
    }
}
