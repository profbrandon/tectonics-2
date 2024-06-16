package util.data.algebraic;

import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic product-type of two datatypes. The recursor is defined
 * by the {@link Prod#destroy(Function)} function which accepts a curried function of two
 * arguments, one for the first element of the pair and the other for the second.
 */
public final class Prod<A, B> {

    private final A first;
    private final B second;

    private Prod(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * The recursor for the {@link Prod} type which supplies the first and second elements of the pair
     * to the curried function.
     * 
     * @param <U> the resultant type
     * @param curried a function to consume the first and second elements
     * @return the resultant value
     * @throws IllegalArgumentException when given a {@code null} object
     */
    public final <U> U destroy(final Function<A, Function<B, U>> curried) {
        Preconditions.throwIfNull(curried, "curried");
        return curried.apply(first).apply(second);
    }

    /**
     * @return the first element of the product
     */
    public final A first() {
        return first; // this.destroy(a -> { return (b -> a); });
    }

    /**
     * @return the second element of the product
     */
    public final B second() {
        return second; // this.destroy(a -> { return (b -> b); });
    }

    /**
     * Determines if this {@link Prod} object is equivalent to another. This method internally uses
     * {@link Object#equals(Object)} to determine if the first and second elements are pointwise equal.
     * 
     * @param pair the other pair to check against
     * @return whether this pair's elements equal the other pairs elements (using {@link Object#equals(Object)})
     * @throws IllegalArgumentException when given a {@code null} object
     */
    public final boolean equalsPair(final Prod<A, B> pair) {
        Preconditions.throwIfNull(pair, "pair");
        return this.first.equals(pair.first) && this.second.equals(pair.second);
    }

    @Override
    public String toString() {
        return "(" + this.first.toString() + ", " + this.second.toString() + ")";
    }

    /**
     * Static method to create a {@link Prod} object by pairing two elements.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param first the first element
     * @param second the second element
     * @return a {@link Prod} of the two elements
     * @throws IllegalArgumentException when given {@code null} objects
     */
    public static <A, B> Prod<A, B> pair(final A first, final B second) {
        Preconditions.throwIfNull(first, "first");
        Preconditions.throwIfNull(second, "second");
        return new Prod<A,B>(first, second);
    }

    /**
     * The staticrecursor for the {@link Prod} type which supplies the first and second elements of
     * the pair to the curried function.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <U> the resultant type
     * @param pair the pair to supply to the curried function
     * @param curried a function to consume the first and second elements
     * @return the resultant value
     * @throws IllegalArgumentException when given {@code null} objects
     */
    public static <A, B, U> U destroy(final Prod<A, B> pair, final Function<A, Function<B, U>> curried) {
        Preconditions.throwIfNull(pair, "pair");
        return pair.destroy(curried);
    }

    /**
     * Maps the first element in a {@link Prod} using the provided mapping function.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <X> the new type of the first element
     * @param pair the pair to map
     * @param function the function mapping the first element to a new element (and possibly type)
     * @return the new pair
     * @throws IllegalArgumentException when given {@code null} objects
     */
    public static <A, B, X> Prod<X, B> mapFirst(final Prod<A, B> pair, final Function<A, X> function) {
        Preconditions.throwIfNull(pair, "pair");
        Preconditions.throwIfNull(function, "function");
        return pair.destroy(a -> b -> Prod.pair(function.apply(a), b));
    }

    /**
     * Maps the second element in a {@link Prod} using the provided mapping function.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <Y> the new type of the second element
     * @param pair the pair to map
     * @param function the function mapping the second element to a new element (and possibly type)
     * @return the new pair
     * @throws IllegalArgumentException when given {@code null} objects
     */
    public static <A, B, Y> Prod<A, Y> mapSecond(final Prod<A, B> pair, final Function<B, Y> function) {
        Preconditions.throwIfNull(pair, "pair");
        Preconditions.throwIfNull(function, "function");
        return pair.destroy(a -> b -> Prod.pair(a, function.apply(b)));
    }

    /**
     * Maps the elements in a {@link Prod} using the provided mapping functions.
     * 
     * @param <A> the type of the first element
     * @param <B> the type of the second element
     * @param <X> the new type of the first element
     * @param <Y> the new type of the second element
     * @param pair the pair to map
     * @param mapFirst the function mapping the first element to a new element (and possibly type)
     * @param mapSecond the function mapping the second element to a new element (and possibly type)
     * @return the new pair
     * @throws IllegalArgumentException when given {@code null} objects
     */
    public static <A, B, X, Y> Prod<X, Y> map(final Prod<A, B> pair, final Function<A, X> mapFirst, final Function<B, Y> mapSecond) {
        Preconditions.throwIfNull(pair, "pair");
        Preconditions.throwIfNull(mapFirst, "mapFirst");
        Preconditions.throwIfNull(mapSecond, "mapSecond");
        return pair.destroy(a -> b -> Prod.pair(mapFirst.apply(a), mapSecond.apply(b)));
    }
}
