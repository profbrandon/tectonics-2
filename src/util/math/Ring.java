package util.math;

import java.util.Collection;

import util.Preconditions;

/**
 * Interface to represent a mathematical ring over the given datatype. Every {@link Ring} is
 * a group under "addition" and a monoid under "multiplication". The multiplication must
 * satisfy the following:
 * <ul>
 *   <li>Multiplicative Identity - {@code r * 1 = 1 * r = r}</li>
 *   <li>Left Distributivity - {@code r * (s + t) = r * s + r * t}</li>
 *   <li>Right Distributivity - {@code (s + t) * r = s * r + t * r}</li>
 * </ul>
 * From these additional laws, several other follow:
 * <ul>
 *   <li>Multiplicative Property of Zero - {@code 0 * r = r * 0 = 0}</li>
 *   <li>Inverse Property of Negative One - {@code (-1) * r = -r}</li>
 * </ul>
 */
public interface Ring<R> extends AbelianGroup<R> {

    /**
     * The multiplicative identity of the ring should satisfy {@code mult(unit(), r) = mult(r, unit()) = r}.
     * 
     * @return the multiplicative identity of the ring
     */
    public R unit();
    
    /**
     * The ring's multiplication should satisfy associativity ({@code mult(r, mult(s, t)) = mult(mult(r, s), t)})
     * as well as the left and right distributive laws. Note that the multiplication is not required to be
     * commutative.
     * 
     * @param r1 the first object
     * @param r2 the second object
     * @return the product of the two
     */
    public R mult(final R r1, final R r2);

    /**
     * Uses the associative ring multiplication ({@link Ring#mult(Object, Object)}) to multiply an arbitrary
     * number of elements. If no elements are provided it returns {@link Ring#unit()}.
     * 
     * @param rs the elements to multiply
     * @return the product of the elements
     */
    public default R multAll(final Collection<R> rs) {
        Preconditions.throwIfContainsNull(rs, "rs");

        R temp = unit();

        for (final R r : rs) {
            temp = mult(temp, r);
        }

        return temp;
    }

    /**
     * Subtraction in the ring defined by {@code r1 - r2 := r1 + (-r2)}.
     * 
     * @param r1 the first argument
     * @param r2 the second argument
     * @return the result of the subtraction
     */
    public default R sub(final R r1, final R r2) {
        return sum(r1, neg(r2));
    }

    /**
     * @return the underlying multiplicative monoid (given by {@link Ring#unit()} and {@link Ring#mult(Object, Object)})
     */
    public default Monoid<NonZero<R>> multiplicativeMonoid() {
        final Monoid<R> monoid = this;

        return new Monoid<>() {
            @Override
            public NonZero<R> zero() {
                return new NonZero<>(unit(), monoid);
            }

            @Override
            public NonZero<R> sum(final NonZero<R> r1, final NonZero<R> r2) {
                return new NonZero<>(mult(r1.get(), r2.get()), monoid);
            }
            
            @Override
            public boolean equiv(final NonZero<R> r1, final NonZero<R> r2) {
                return monoid.equiv(r1.get(), r2.get());
            }
        };
    }
}
