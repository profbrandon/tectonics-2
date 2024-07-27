package util.math.monoids;

import java.util.function.Function;

import util.data.algebraic.List;
import util.data.algebraic.Unit;
import util.math.Monoid;
import util.math.Nat;

/**
 * Class to represent the natural numbers (equivalent to the free monoid on one object '0') which
 * is initial in the space of monoids
 */
public class Naturals
    implements Nat<List<Unit>> {

    public static final Nat<List<Unit>> INSTANCE = new Naturals();

    private Naturals() {}

    @Override
    public List<Unit> natZero() {
        return List.empty();
    }

    @Override
    public List<Unit> natSucc(final List<Unit> a) {
        return List.cons(Unit.unit(), a);
    }

    @Override
    public <U> U recurseNat(final List<Unit> a, final U z, final Function<List<Unit>, Function<U, U>> s) {
        if (equiv(a, natZero())) {
            return z;
        } else {
            return s.apply(a).apply(recurseNat(a.tail(), z, s));
        }
    }

    @Override
    public boolean equiv(final List<Unit> a1, final List<Unit> a2) {
        return a1.equalsList(a2, pair -> pair.destroy(x -> y -> x.equals(y)));
    }

    public static <A> Function<List<Unit>, A> universalNat(final Nat<A> other) {
        return n -> INSTANCE.iterateNat(n, other.zero(), other::natSucc);
    }

    /**
     * Morphism to any monoid with at least one object.
     * 
     * @param <A> the underlying type of the given monoid
     * @param monoid the monoid to produce a morphism to
     * @param one the "1" object that will be iteratively summed
     * @return a function from the natural numbers to the monoid
     */
    public static <A> Function<List<Unit>, A> universalMonoidOn1(final Monoid<A> monoid, final A one) {
        return n -> INSTANCE.iterateNat(n, monoid.zero(), x -> monoid.sum(x, one));
    }
}
