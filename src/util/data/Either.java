package util.data;

import java.util.Optional;
import java.util.function.Function;

import util.Preconditions;

public class Either<A, B> {
    
    private final Optional<A> left;
    private final Optional<B> right;

    private Either(final Optional<A> left, final Optional<B> right) {
        this.left = left;
        this.right = right;
    }

    public final <U> U match(final Function<A, U> leftCase, final Function<B, U> rightCase) {
        return this.left.map(leftCase).orElseGet(() -> rightCase.apply(this.right.get()));
    }

    public final boolean equalsEither(final Either<A,B> other) {
        return this.match(
            a -> other.match(
                x -> a.equals(x),
                y -> false),
            b -> other.match(
                x -> false,
                y -> b.equals(y)));
    }

    public final Optional<A> forgetRight() {
        return Either.forgetRight(this);
    }

    public final Optional<B> forgetLeft() {
        return Either.forgetLeft(this);
    }

    @Override
    public String toString() {
        return match(
            a -> "Left(" + a.toString() + ")",
            b -> "Right(" + b.toString() + ")");
    }

    public static <A,B> Either<A,B> left(final A left) {
        Preconditions.throwIfNull(left, "left");
        return new Either<>(Optional.of(left), Optional.empty());
    }

    public static <A,B> Either<A,B> right(final B right) {
        Preconditions.throwIfNull(right, "right");
        return new Either<>(Optional.empty(), Optional.of(right));
    }

    public static <A,B,U> U match(final Either<A, B> sum, final Function<A, U> leftCase, final Function<B, U> rightCase) {
        return sum.match(leftCase, rightCase);
    }

    public static <A> A collapse(final Either<A, A> sum) {
        return sum.match(
            a -> a,
            a -> a);
    }

    public static <A, B> Optional<A> forgetRight(final Either<A, B> sum) {
        return sum.match(
            a -> Optional.of(a),
            b -> Optional.empty());
    }

    public static <A, B> Optional<B> forgetLeft(final Either<A, B> sum) {
        return sum.match(
            a -> Optional.empty(),
            b -> Optional.of(b));
    }

    public static <A, B, X> Either<X, B> mapLeft(final Either<A, B> sum, final Function<A, X> function) {
        return sum.match(
            a -> Either.left(function.apply(a)), 
            Either::right);
    }

    public static <A, B, Y> Either<A, Y> mapRight(final Either<A, B> sum, final Function<B, Y> function) {
        return sum.match(
            Either::left, 
            b -> Either.right(function.apply(b)));
    }
}
