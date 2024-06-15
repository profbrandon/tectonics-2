package util.data.algebraic;

import java.util.Optional;
import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic sum-type of two datatypes. The recursor is defined by the 
 * {@link Either#match(Function, Function)} method which pattern matches on the {@link Either#left()} or
 * {@link Either#right()} value.
 */
public final class Either<A, B> {
    private final Optional<A> left;
    private final Optional<B> right;

    private Either(final Optional<A> left, final Optional<B> right) {
        this.left = left;
        this.right = right;
    }

    /**
     * The recursor for the sum-type which pattern matches on this sum.
     * 
     * @param <U> the resultant type
     * @param leftCase the function to be applied upon an {@link Either#left()} pattern match success
     * @param rightCase the function to be applied upon an {@link Either#right()} pattern match success
     * @return the result of either the application of the leftCase or rightCase functions
     */
    public final <U> U match(final Function<A, U> leftCase, final Function<B, U> rightCase) {
        return this.left.map(leftCase).orElseGet(() -> rightCase.apply(this.right.get()));
    }

    /**
     * Determines if this {@link Either} object is equivalent to another. Utilizes the base {@link Object#equals(Object)}
     * function on the two summand datatypes.
     * 
     * @param other the other {@link Either} object
     * @return whether the two objects are equal by determining if the contained sub-object is equivalent
     */
    public final boolean equalsEither(final Either<A,B> other) {
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
        return Either.forgetRight(this);
    }

    /**
     * @return an optional possibly containing an object of the right type
     */
    public final Optional<B> forgetLeft() {
        return Either.forgetLeft(this);
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
     * @return an {@link Either} object containing the left object
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A,B> Either<A,B> left(final A left) {
        Preconditions.throwIfNull(left, "left");
        return new Either<>(Optional.of(left), Optional.empty());
    }

    /**
     * @param <A> the left summand datatype
     * @param <B> the right summand datatype
     * @param right the right object
     * @return an {@link Either} object containing the right object
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public static <A,B> Either<A,B> right(final B right) {
        Preconditions.throwIfNull(right, "right");
        return new Either<>(Optional.empty(), Optional.of(right));
    }

    /**
     * The recursor for the sum-type which pattern matches on the provided sum.
     * 
     * @param <A> the left summand datatype
     * @param <B> the right summand datatype
     * @param <U> the resultant type
     * @param sum the {@link Either} object to pattern match on
     * @param leftCase the function to be applied upon an {@link Either#left()} pattern match success
     * @param rightCase the function to be applied upon an {@link Either#right()} pattern match success
     * @return the result of either the application of the leftCase or rightCase functions
     */
    public static <A,B,U> U match(final Either<A, B> sum, final Function<A, U> leftCase, final Function<B, U> rightCase) {
        return sum.match(leftCase, rightCase);
    }

    /**
     * Collapses an {@link Either} of the same type twice to a single value.
     * 
     * @param <A> the resultant and summand types
     * @param sum the {@link Either} object to destroy
     * @return a value of the type A
     */
    public static <A> A collapse(final Either<A, A> sum) {
        return sum.match(
            a -> a,
            a -> a);
    }

    /**
     * @param sum the {@link Either} object to destroy
     * @return an optional possibly containing an object of the left type
     */
    public static <A, B> Optional<A> forgetRight(final Either<A, B> sum) {
        return sum.match(
            a -> Optional.of(a),
            b -> Optional.empty());
    }

    /**
     * @param sum the {@link Either} object to destroy
     * @return an optional possibly containing an object of the right type
     */
    public static <A, B> Optional<B> forgetLeft(final Either<A, B> sum) {
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
     * @param sum the {@link Either} object to pattern match on
     * @param function the left summand type transformation
     * @return a new {@link Either} object with the new left summand type
     */
    public static <A, B, X> Either<X, B> mapLeft(final Either<A, B> sum, final Function<A, X> function) {
        return sum.match(
            a -> Either.left(function.apply(a)), 
            Either::right);
    }

    /**
     * Transforms the right type of the sum based on the provided function.
     * 
     * @param <A> the left summand type
     * @param <B> the right summand type
     * @param <Y> the new right summand type
     * @param sum the {@link Either} object to pattern match on
     * @param function the right summand type transformation
     * @return a new {@link Either} object with the new right summand type
     */
    public static <A, B, Y> Either<A, Y> mapRight(final Either<A, B> sum, final Function<B, Y> function) {
        return sum.match(
            Either::left, 
            b -> Either.right(function.apply(b)));
    }
}
