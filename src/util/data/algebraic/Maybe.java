package util.data.algebraic;

import java.util.function.Function;

public class Maybe<A> {

    private final Sum<Unit, A> value;

    private Maybe(final Sum<Unit, A> value) {
        this.value = value;
    }

    public Sum<Unit, A> asSum() {
        return value;
    }

    public <U> U match(final Function<Unit, U> onNothing, final Function<A, U> onJust) {
        return value.match(onNothing, onJust);
    }

    public static <A> Maybe<A> just(final A value) {
        return new Maybe<>(Sum.right(value));
    }

    public static <A> Maybe<A> nothing() {
        return new Maybe<>(Sum.left(Unit.unit()));
    }

    // Maybe Monad

    public static <A> Maybe<A> ret(final A a) {
        return just(a);
    }

    public static <A> Maybe<A> join(final Maybe<Maybe<A>> mma) {
        return bind(mma, ma -> ma);
    }

    public static <A, B> Maybe<B> bind(final Maybe<A> ma, final Function<A, Maybe<B>> kleisli) {
        return ma.match(
            __ -> Maybe.<B>nothing(), 
            a  -> kleisli.apply(a));
    }

    public static <A, B> Maybe<B> seq(final Maybe<A> ma, final Maybe<B> mb) {
        return bind(ma, __ -> mb);
    }

    public static Maybe<Unit> guard(boolean test) {
        return test ? just(Unit.unit()) : nothing();
    }
}
