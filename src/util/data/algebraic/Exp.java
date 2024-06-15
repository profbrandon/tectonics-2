package util.data.algebraic;

import java.util.function.Function;

public final class Exp<A,B> implements Function<A,B> {

    private final Function<A, B> function;
    
    private Exp(final Function<A, B> function) {
        this.function = function;
    }

    public final <U> Function<Prod<A, B>, U> curry(final Function<A, Exp<B, U>> transpose) {
        return pair -> transpose.apply(pair.first()).apply(pair.second());
    }

    public final <U> Function<A, Exp<B, U>> uncurry(final Function<Prod<A, B>, U> transpose) {
        return a -> Exp.asExponential(b -> transpose.apply(Prod.pair(a,b)));
    }

    public final <X> Exp<X, B> after(final Exp<X, A> before) {
        return Exp.asExponential(x -> this.apply(before.apply(x)));
    }

    @Override
    public B apply(A t) {
        return this.function.apply(t);
    }

    public boolean equalsExp(final Exp<A, B> other) {
        return this.function.equals(other.function);
    }
    
    public static <A, B> Exp<A, B> asExponential(final Function<A, B> function) {
        return new Exp<>(function);
    }

    public static <A> Exp<A, A> identity() {
        return Exp.asExponential(a -> a);
    }
}
