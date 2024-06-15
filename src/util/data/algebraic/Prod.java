package util.data.algebraic;

import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic product-type of two datatypes
 */
public final class Prod<A,B> {

    private final A first;
    private final B second;

    private Prod(final A first, final B second) {
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

    public final boolean equalsPair(final Prod<A,B> pair) {
        return this.first.equals(pair.first) && this.second.equals(pair.second);
    }

    @Override
    public String toString() {
        return "(" + this.first.toString() + ", " + this.second.toString() + ")";
    }

    public static <A,B> Prod<A,B> pair(final A first, final B second) {
        Preconditions.throwIfNull(first, "first");
        Preconditions.throwIfNull(second, "second");

        return new Prod<A,B>(first, second);
    }

    public static <A,B,U> U destroy(final Prod<A,B> pair, final Function<A, Function<B, U>> curried) {
        return pair.destroy(curried);
    }

    public static <A,B,X> Prod<X,B> mapFirst(final Prod<A,B> pair, final Function<A, X> function) {
        return pair.destroy(a -> b -> Prod.pair(function.apply(a), b));
    }

    public static <A,B,Y> Prod<A,Y> mapSecond(final Prod<A, B> pair, final Function<B, Y> function) {
        return pair.destroy(a -> b -> Prod.pair(a, function.apply(b)));
    }
}
