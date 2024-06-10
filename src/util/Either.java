package util;

import java.util.Optional;
import java.util.function.Function;

public class Either<A, B> {
    
    private final Optional<A> left;
    private final Optional<B> right;

    private Either(final Optional<A> left, final Optional<B> right) {
        this.left = left;
        this.right = right;
    }

    public final <U> U match(final Function<A, U> leftCase, final Function<B, U> rightCase) {
        return left.map(leftCase).orElse(rightCase.apply(right.get()));
    }

    public final boolean equals(final Either<A,B> other) {
        return this.match(a -> other.match(x -> a.equals(x), y -> false), b -> other.match(x -> false, y -> b.equals(y)));
    }

    public final Optional<A> forgetRight() {
        return Either.forgetRight(this);
    }

    public final Optional<B> forgetLeft() {
        return Either.forgetLeft(this);
    }

    public static <A,B> Either<A,B> left(final A left) {
        return new Either<>(Optional.of(left), Optional.empty());
    }

    public static <A,B> Either<A,B> right(final B right) {
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
            b -> Either.right(b));
    }

    public static <A, B, Y> Either<A, Y> mapRight(final Either<A, B> sum, final Function<B, Y> function) {
        return sum.match(
            a -> Either.left(a), 
            b -> Either.right(function.apply(b)));
    }
}
