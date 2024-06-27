package util.data.algebraic;

public final class Identities {

    public static <A> A eitherEmptyLeft(final Sum<Empty, A> sum) {
        return sum.match(
            Empty::absurd, 
            a -> a);
    }

    public static <A> Sum<Empty, A> eitherEmptyLeftInverse(final A value) {
        return Sum.right(value);
    }

    public static <A> A eitherEmptyRight(final Sum<A, Empty> sum) {
        return sum.match(
            a -> a, 
            Empty::absurd);
    }

    public static <A> Sum<A, Empty> eitherEmptyRightInverse(final A value) {
        return Sum.left(value);
    }

    public static <A> A prodUnitLeft(final Prod<Unit, A> pair) {
        return pair.destroy(u -> a -> u.destroy(a));
    }

    public static <A> Prod<Unit, A> prodUnitLeftInverse(final A value) {
        return Prod.pair(Unit.unit(), value);
    }

    public static <A> A prodUnitRight(final Prod<A, Unit> pair) {
        return pair.destroy(a -> u -> u.destroy(a));
    }

    public static <A> Prod<A, Unit> prodUnitRightInverse(final A value) {
        return Prod.pair(value, Unit.unit());
    }

    public static <A> Empty prodEmptyLeft(final Prod<Empty, A> pair) {
        return pair.first();
    }

    public static <A> Prod<Empty, A> prodEmptyLeftInverse(final Empty empty) {
        return Prod.pair(empty, Empty.absurd(empty));
    }

    public static <A> Empty prodEmptyRight(final Prod<A, Empty> pair) {
        return pair.second();
    }

    public static <A> Prod<A, Empty> prodEmptyRightInverse(final Empty empty) {
        return Prod.pair(Empty.absurd(empty), empty);
    }

    public static <A, B, C> Sum<Sum<A, B>, C> eitherAssociate(final Sum<A, Sum<B, C>> sum) {
        return sum.match(
            a -> Sum.left(Sum.left(a)),
            sum1 -> sum1.match(
                b -> Sum.left(Sum.right(b)),
                c -> Sum.right(c)));
    }

    public static <A, B, C> Sum<A, Sum<B, C>> eitherAssociateInverse(final Sum<Sum<A, B>, C> sum) {
        return sum.match(
            sum1 -> sum1.match(
                a -> Sum.left(a),
                b -> Sum.right(Sum.left(b))), 
            c -> Sum.right(Sum.right(c)));
    }

    public static <A, B, C> Prod<Prod<A, B>, C> prodAssociate(final Prod<A, Prod<B, C>> pair) {
        return pair.destroy(
            a -> 
                bc -> bc.destroy(
                    b ->
                        c -> Prod.pair(Prod.pair(a, b), c)));
    }

    public static <A, B, C> Prod<A, Prod<B, C>> prodAssociateInverse(final Prod<Prod<A, B>, C> pair) {
        return pair.destroy(
            ab ->
                c -> ab.destroy(
                    a -> 
                        b -> Prod.pair(a, Prod.pair(b, c))));
    }

    public static <A, B> Sum<B, A> eitherCommute(final Sum<A, B> sum) {
        return sum.match(
            a -> Sum.right(a),
            b -> Sum.left(b));
    }

    public static <A, B> Prod<B, A> prodCommute(final Prod<A, B> pair) {
        return pair.destroy(a -> b -> Prod.pair(b, a));
    }

    public static <A, B, C> Sum<Prod<A, B>, Prod<A, C>> prodDistribute(final Prod<A, Sum<B, C>> pair) {
        return Sum.map(
            pair.second(),
            b -> Prod.pair(pair.first(), b),
            c -> Prod.pair(pair.first(), c));
    }

    public static <A, B, C> Prod<A, Sum<B, C>> prodDistributeInverse(final Sum<Prod<A, B>, Prod<A, C>> sum) {
        return sum.match(
            ab -> Prod.pair(ab.first(), Sum.left(ab.second())),
            ac -> Prod.pair(ac.first(), Sum.right(ac.second())));
    }

    public static <A, B, C> Prod<Exp<A, C>, Exp<B, C>> expSum(final Exp<Sum<A, B>, C> function) {
        return Prod.pair(
            Exp.asExponential(a -> function.apply(Sum.left(a))),
            Exp.asExponential(b -> function.apply(Sum.right(b))));
    }

    public static <A, B, C> Exp<Sum<A, B>, C> expSumInverse(final Prod<Exp<A, C>, Exp<B, C>> pair) {
        return Exp.asExponential(either -> either.match(pair.first(), pair.second()));
    }

    public static <A, B, C> Prod<Exp<A, B>, Exp<A, C>> expDistribute(final Exp<A, Prod<B, C>> function) {
        return Prod.pair(
            Exp.asExponential(a -> function.apply(a).first()),
            Exp.asExponential(a -> function.apply(a).second()));
    }

    public static <A, B, C> Exp<A, Prod<B, C>> expDistributeInverse(final Prod<Exp<A, B>, Exp<A, C>> pair) {
        return Exp.asExponential(a -> Prod.pair(pair.first().apply(a), pair.second().apply(a)));
    }

    public static <A, B, C> Exp<Prod<A, B>, C> expOfExp(final Exp<A, Exp<B, C>> exp) {
        return Exp.asExponential(pair -> Exp.eval(Exp.eval(exp, pair.first()), pair.second()));
    }

    public static <A, B, C> Exp<A, Exp<B, C>> expOfExpInverse(final Exp<Prod<A, B>, C> exp) {
        return Exp.asExponential(a -> Exp.asExponential(b -> Exp.eval(exp, Prod.pair(a, b))));
    }
}
