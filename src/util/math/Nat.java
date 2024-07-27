package util.math;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * Interface to represent a space that contains a natural numbers monoid.
 */
public interface Nat<A> extends Monoid<A> {

    /**
     * @return the zero
     */
    public A natZero();

    /**
     * @param a the value to "increment"
     * @return the successor of the given value
     */
    public A natSucc(final A a);

    /**
     * The recursor must satisfy the following relations:
     * 
     * <ul>
     *   <li>{@code recurseNat(natZero(), z, s) == z}</li>
     *   <li>{@code recurseNat(natSucc(a), z, s) == s(a, recurseNat(a, z, s))}</li>
     * </ul>
     * 
     * @param <U> the target type
     * @param a the value to recurse on
     * @param z the zero value
     * @param s the successor function
     * @return an element of the target type
     */
    public <U> U recurseNat(final A a, final U z, final Function<A, Function<U, U>> s);

    /**
     * The derived iterator from the recursor. Iteration does not care about which value
     * it is currently operating on.
     * 
     * @param <U> the target type
     * @param a the value to iterate on
     * @param z the zero value
     * @param it the function to iterate
     * @return an element of the target type
     */
    default <U> U iterateNat(final A a, final U z, final Function<U, U> it) {
        return recurseNat(a, z, __ -> it);
    }

    /**
     * @param a the value to check
     * @return whether the given value is equal to the zero value
     */
    default boolean isNatZero(final A a) {
        return iterateNat(a, true, x -> false);
    }

    /**
     * @return the "one" value
     */
    default A natOne() {
        return natSucc(zero());
    }

    /**
     * The predecessor function, which returns {@link Nat#natZero()} on a zero value.
     * 
     * @param a the value to "decrement"
     * @return the predecessor of the given value
     */
    default A natPred(final A a) {
        return recurseNat(a, zero(), p -> x -> isNatZero(p) ? zero() : natSucc(x));
    }

    /**
     * Function to multiply two natural numbers.
     * 
     * @param a1 the first number
     * @param a2 the second number
     * @return the result of the multiplication
     */
    default A natMult(final A a1, final A a2) {
        return iterateNat(a1, zero(), x -> sum(x, a2));
    }

    /**
     * Function to exponentiate two natural numbers.
     * 
     * @param a1 the base
     * @param a2 the exponent
     * @return the exponentiation of the two numbers
     */
    default A natExp(final A a1, final A a2) {
        return iterateNat(a2, natOne(), x -> natMult(x, a1));
    }

    /**
     * Recurses on the given values to test for equivalence (equidistant from zero).
     * 
     * @param a1 the first value
     * @param a2 the second value
     * @return whether the two values are equal
     */
    default boolean equalsNat(final A a1, final A a2) {
        return recurseNat(a1, isNatZero(a2), p -> __ -> equalsNat(p, natPred(a2)));
    }

    /**
     * Converts the value to an integer.
     * 
     * @param a the value to convert
     * @return the integer version of the given value
     */
    default BigInteger asInteger(final A a) {
        return iterateNat(a, BigInteger.ZERO, u -> u.add(BigInteger.ONE));
    }

    @Override
    default A zero() {
        return natZero();
    }

    @Override
    default A sum(final A m1, final A m2) {
        return iterateNat(m2, m1, this::natSucc);   
    }
}
