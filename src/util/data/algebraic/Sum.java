package util.data.algebraic;

import java.util.Optional;
import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic sum-type of two datatypes. The recursor is defined by the 
 * {@link Sum#match(Function, Function)} method which pattern matches on the {@link Sum#left()} or
 * {@link Sum#right()} value.
 */
public final class Sum<A, B> {

    private final Optional<A> left;
    private final Optional<B> right;

    private Sum(final Optional<A> left, final Optional<B> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * The recursor for the sum-type which pattern matches on this sum.
     * 
     * @param <U> the resultant type
     * @param leftCase the function to be applied upon an {@link Sum#left()} pattern match success
     * @param rightCase the function to be applied upon an {@link Sum#right()} pattern match success
     * @return the result of either the application of the leftCase or rightCase functions
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public final <U> U match(final Function<A, U> leftCase, final Function<B, U> rightCase) {
        Preconditions.throwIfNull(leftCase, "leftCase");
        Preconditions.throwIfNull(rightCase, "rightCase");
        return this.left.map(leftCase).orElseGet(() -> rightCase.apply(this.right.get()));
    }

    /**
     * Determines if this {@link Sum} object is equivalent to another. Utilizes the base {@link Object#equals(Object)}
     * function on the two summand datatypes.
     * 
     * @param other the other {@link Sum} object
     * @return whether the two objects are equal by determining if the contained sub-object is equivalent
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public final boolean equalsEither(final Sum<A,B> other) {
        Preconditions.throwIfNull(other, "other");
        return this.match(
            a -> other.match(
                x -> a.equals(x),
                y -> false),
            b -> other.match(
                x -> false,
                y -> b.equals(y)));
    }

    /**
     * @return an optional possibly containing an object of the left type
     */
    public final Optional<A> forgetRight() {
        return Sum.forgetRight(this);
    }

    /**
     * @return an optional possibly containing an object of the right type
     */
    public final Optional<B> forgetLeft() {
        return Sum.forgetLeft(this);
    }

    @Override
    public String toString() {
        return match(
            a -> "Left(" + a.toString() + ")",
            b -> "Right(" + b.toString() + ")");
    }

    /**
     * @param <A> the left summand datatype
     * @param <B> the right summand datatype
     * @param left the left object
     * @return an {@link Sum} object containing the left object
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A,B> Sum<A,B> left(final A left) {
        Preconditions.throwIfNull(left, "left");
        return new Sum<>(Optional.of(left), Optional.empty());
    }

    /**
     * @param <A> the left summand datatype
     * @param <B> the right summand datatype
     * @param right the right object
     * @return an {@link Sum} object containing the right object
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A,B> Sum<A,B> right(final B right) {
        Preconditions.throwIfNull(right, "right");
        return new Sum<>(Optional.empty(), Optional.of(right));
    }

    /**
     * The recursor for the sum-type which pattern matches on the provided sum.
     * 
     * @param <A> the left summand datatype
     * @param <B> the right summand datatype
     * @param <U> the resultant type
     * @param sum the {@link Sum} object to pattern match on
     * @param leftCase the function to be applied upon an {@link Sum#left()} pattern match success
     * @param rightCase the function to be applied upon an {@link Sum#right()} pattern match success
     * @return the result of either the application of the leftCase or rightCase functions
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A,B,U> U match(final Sum<A, B> sum, final Function<A, U> leftCase, final Function<B, U> rightCase) {
        Preconditions.throwIfNull(sum, "sum");
        return sum.match(leftCase, rightCase);
    }

    /**
     * Collapses an {@link Sum} of the same type twice to that single type.
     * 
     * @param <A> the resultant and summand types
     * @param sum the {@link Sum} object to destroy
     * @return a value of the type A
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A> A collapse(final Sum<A, A> sum) {
        Preconditions.throwIfNull(sum, "sum");
        return sum.match(
            a -> a,
            a -> a);
    }

    /**
     * @param sum the {@link Sum} object to destroy
     * @return an optional possibly containing an object of the left type
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A, B> Optional<A> forgetRight(final Sum<A, B> sum) {
        Preconditions.throwIfNull(sum, "sum");
        return sum.match(
            a -> Optional.of(a),
            b -> Optional.empty());
    }

    /**
     * @param sum the {@link Sum} object to destroy
     * @return an optional possibly containing an object of the right type
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A, B> Optional<B> forgetLeft(final Sum<A, B> sum) {
        Preconditions.throwIfNull(sum, "sum");
        return sum.match(
            a -> Optional.empty(),
            b -> Optional.of(b));
    }

    /**
     * Transforms the left type of the sum based on the provided function.
     * 
     * @param <A> the left summand type
     * @param <B> the right summand type
     * @param <X> the new left summand type
     * @param sum the {@link Sum} object to pattern match on
     * @param function the left summand type transformation
     * @return a new {@link Sum} object with the new left summand type
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B, X> Sum<X, B> mapLeft(final Sum<A, B> sum, final Function<A, X> function) {
        Preconditions.throwIfNull(sum, "sum");
        Preconditions.throwIfNull(function, "function");
        return sum.match(
            a -> Sum.left(function.apply(a)), 
            Sum::right);
    }

    /**
     * Transforms the right type of the sum based on the provided function.
     * 
     * @param <A> the left summand type
     * @param <B> the right summand type
     * @param <Y> the new right summand type
     * @param sum the {@link Sum} object to pattern match on
     * @param function the right summand type transformation
     * @return a new {@link Sum} object with the new right summand type
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B, Y> Sum<A, Y> mapRight(final Sum<A, B> sum, final Function<B, Y> function) {
        Preconditions.throwIfNull(sum, "sum");
        Preconditions.throwIfNull(function, "function");
        return sum.match(
            Sum::left, 
            b -> Sum.right(function.apply(b)));
    }

    /**
     * Transforms an {@link Sum} object by conditionally applying either the left map or right map to produce
     * a new {@link Sum} object
     * @param <A> the left summand type
     * @param <B> the right summand type
     * @param <X> the new left summand type
     * @param <Y> the new right summand type
     * @param sum the {@link Sum} object to pattern match on
     * @param mapLeft the left summand type transformation
     * @param mapRight the right summand type transformation
     * @return a new {@link Sum} object with the new left and right summand types
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public static <A, B, X, Y> Sum<X, Y> map(final Sum<A, B> sum, final Function<A, X> mapLeft, final Function<B, Y> mapRight) {
        Preconditions.throwIfNull(sum, "sum");
        Preconditions.throwIfNull(mapLeft, "mapLeft");
        Preconditions.throwIfNull(mapRight, "mapRight");
        return sum.match(
            a -> Sum.left(mapLeft.apply(a)),
            b -> Sum.right(mapRight.apply(b)));
    }
}
