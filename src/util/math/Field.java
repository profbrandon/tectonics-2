package util.math;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.vectorspaces.VectorSpace;

/**
 * Interface to represent a mathematical field over the given datatype. Every {@link Field} is a 
 * {@link Ring} where the nonzero elements form a {@link AbelianGroup} under multiplication.
 */
public interface Field<Q> extends Ring<Q>, VectorSpace<Q, Q> {
    
    /**
     * Produces the multiplicative inverse for any nonzero value, otherwise produces
     * the {@link Unit#unit()}.
     * 
     * @param q the value to produce a multiplicative inverse of
     * @return either the {@link Unit#unit()} on a zero argument or the inverse
     */
    public Sum<Unit, Q> inv(final Q q);

    /**
     * @return the Abelian (commutative) group of the nonzero elements under multiplication
     */
    default AbelianGroup<NonZero<Q>> multiplicativeGroup() {
        final Monoid<Q> monoid = this;

        return new AbelianGroup<NonZero<Q>>() {
            @Override
            public NonZero<Q> zero() {
                return new NonZero<>(unit(), monoid);
            }

            @Override
            public NonZero<Q> neg(final NonZero<Q> g) {
                final Sum<Unit, Q> inverse = inv(g.get());
                return inverse.match(
                    unit -> { throw new IllegalStateException("Inversion should not have been supplied with a zero."); },
                    q -> new NonZero<>(q, monoid));
            }

            @Override
            public NonZero<Q> sum(final NonZero<Q> m1, final NonZero<Q> m2) {
                return new NonZero<>(mult(m1.get(), m2.get()), monoid);
            }

            @Override
            public boolean equiv(final NonZero<Q> a1, final NonZero<Q> a2) {
                return monoid.equiv(a1.get(), a2.get());
            }
        };
    }
}
