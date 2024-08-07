package util.math.vectorspaces;

import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;
import util.math.fields.Complex;

/**
 * Class to represent the quaternions over some field. Note that this is not a field because the
 * multiplication is not commutative. 
 */
public class Quaternions<R>
    implements
        VectorSpace<Prod<Prod<R, R>, Prod<R, R>>, R> {

    public final Prod<Prod<R, R>, Prod<R, R>> UNIT_R;
    public final Prod<Prod<R, R>, Prod<R, R>> UNIT_I;
    public final Prod<Prod<R, R>, Prod<R, R>> UNIT_J;
    public final Prod<Prod<R, R>, Prod<R, R>> UNIT_K;

    private final Field<R> FIELD;
    private final Complex<R> COMPLEX;
    private final Complex<Prod<R, R>> QUATERNIONS;

    /**
     * Creates the quaternions for the underlying field.
     * 
     * @param underlyingField the field over which to build quaternions (should not be an instance of {@link Complex})
     */
    public Quaternions(final Field<R> underlyingField) {
        this.FIELD       = underlyingField;
        this.COMPLEX     = new Complex<>(underlyingField);
        this.QUATERNIONS = new Complex<>(COMPLEX);

        this.UNIT_R = QUATERNIONS.complex(COMPLEX.UNIT_R, COMPLEX.zero());
        this.UNIT_I = QUATERNIONS.complex(COMPLEX.UNIT_I, COMPLEX.zero());
        this.UNIT_J = QUATERNIONS.complex(COMPLEX.zero(), COMPLEX.UNIT_R);
        this.UNIT_K = QUATERNIONS.complex(COMPLEX.zero(), COMPLEX.UNIT_I);
    }

    /**
     * Constructs a quaternion using the following information.
     * 
     * @param a the real component
     * @param b the "i" component
     * @param c the "j" component
     * @param d the "k" component
     * @return the corresponding quaternion
     */
    public Prod<Prod<R, R>, Prod<R, R>> quaternion(final R a, final R b, final R c, final R d) {
        return QUATERNIONS.complex(COMPLEX.complex(a, b), COMPLEX.complex(c, d));
    }

    /**
     * Extracts the real number part of the quaternion.
     */
    public R real(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return COMPLEX.real(QUATERNIONS.real(q));
    }

    /**
     * Extracts the "i" component from the quaternion.
     */
    public R i(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return COMPLEX.imaginary(QUATERNIONS.real(q));
    }

    /**
     * Extracts the "j" component from the quaternion.
     */
    public R j(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return COMPLEX.real(QUATERNIONS.imaginary(q));
    }

    /**
     * Extracts the "k" component from the quaternion.
     */
    public R k(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return COMPLEX.imaginary(QUATERNIONS.imaginary(q));
    }

    /**
     * Multiplies two quaternions. Note that this is a non-commutative multiplication.
     * 
     * @param q1 the first quaternion
     * @param q2 the second quaternion
     * @return the product
     */
    public Prod<Prod<R, R>, Prod<R, R>> mult(final Prod<Prod<R, R>, Prod<R, R>> q1, final Prod<Prod<R, R>, Prod<R, R>> q2) {
        return QUATERNIONS.complex(
            COMPLEX.sub(COMPLEX.mult(q1.first(), q2.first()), COMPLEX.mult(COMPLEX.conjugate(q2.second()), q1.second())),
            COMPLEX.sum(COMPLEX.mult(q2.second(), q1.first()), COMPLEX.mult(q1.second(), COMPLEX.conjugate(q2.first()))));
    }

    /**
     * Computes the inverse quaternion with respect to the non-commutative multiplication.
     * 
     * @param q
     * @return
     */
    public Sum<Unit, Prod<Prod<R, R>, Prod<R, R>>> inv(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return Sum.map(
            FIELD.inv(magnitude2(q)),
                __     -> Unit.unit(),
                length -> scale(conjugate(q), length));
    }

    /**
     * Computes the conjugate quaternion {@code a - ib - jc - kd}.
     * 
     * @param q the quaternion to build a conjugate from
     * @return the conjugate of the given quaternion
     */
    public Prod<Prod<R, R>, Prod<R, R>> conjugate(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return quaternion(
            real(q), 
            FIELD.neg(i(q)), 
            FIELD.neg(j(q)), 
            FIELD.neg(k(q)));
    }

    /**
     * Computes the squared distance to the origin.
     * 
     * @param q the quaternion to measure
     * @return the squared distance to the origin
     */
    public R magnitude2(final Prod<Prod<R, R>, Prod<R, R>> q) {
        return real(mult(q, conjugate(q)));
    }

    @Override
    public Prod<Prod<R, R>, Prod<R, R>> neg(final Prod<Prod<R, R>, Prod<R, R>> g) {
        return QUATERNIONS.neg(g);
    }

    @Override
    public Prod<Prod<R, R>, Prod<R, R>> zero() {
        return QUATERNIONS.zero();
    }

    @Override
    public Prod<Prod<R, R>, Prod<R, R>> sum(final Prod<Prod<R, R>, Prod<R, R>> m1, final Prod<Prod<R, R>, Prod<R, R>> m2) {
        return QUATERNIONS.sum(m1, m2);
    }

    @Override
    public boolean equiv(final Prod<Prod<R, R>, Prod<R, R>> a1, final Prod<Prod<R, R>, Prod<R, R>> a2) {
        return QUATERNIONS.equiv(a1, a2);
    }

    @Override
    public Field<R> underlyingField() {
        return FIELD;
    }

    @Override
    public Prod<Prod<R, R>, Prod<R, R>> scale(final Prod<Prod<R, R>, Prod<R, R>> v, final R scalar) {
        return QUATERNIONS.mult(v, QUATERNIONS.fromReal(COMPLEX.fromReal(scalar)));
    }
}
