package util.math;

import java.util.Collection;

import util.Preconditions;

/**
 * Interface to represent a mathematical monoid over the given datatype. Monoids come 
 * equipped with a zero object ({@link Monoid#zero()}) and an associative "addition"
 * ({@link Monoid#sum(Object, Object)}) such that the zero is an identity of the addition, i.e.,
 * <ul>
 *   <li>Identity - {@code 0 + m = m + 0 = m}</li>
 *   <li>Associativity - {@code m + (n + k) = (m + n) + k}</li>
 * </ul>
 */
public interface Monoid<M> extends Equiv<M> {
    
    /**
     * The zero object should satisfy {@code sum(0, m) = sum(m, 0) = m} for any {@code m}.
     * 
     * @return the zero object
     */
    public M zero();

    /**
     * The monoidal addition should satisfy associativity: {@code sum(m, sum(n, k)) = sum(sum(m, n), k)}.
     * Note that it is not required to satisfy commutivity.
     * 
     * @param m1 the first summand
     * @param m2 the second summand
     * @return the resulting monoidal "sum" of the two objects
     */
    public M sum(final M m1, final M m2);

    /**
     * Uses the associative monoidal addition ({@link Monoid#sum(Object, Object)}) to sum an arbitrary
     * number of elements. If no elements are provided it returns {@link Monoid#zero()}.
     * 
     * @param ms the elements to sum
     * @return the sum of the elements
     */
    public default M sumAll(final Collection<M> ms) {
        Preconditions.throwIfNull(ms, "ms");
        Preconditions.throwIfContainsNull(ms, "ms");

        M temp = zero();

        for (final M v : ms) {
            temp = sum(temp, v);
        }

        return temp;
    }
}
