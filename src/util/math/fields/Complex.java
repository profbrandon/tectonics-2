package util.math.fields;

import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;

/**
 * Class to represent the complex extension of some other field. In other words, this adjoins
 * a value {@code i} defined to satisify the equation {@code i * i = -1}. Alternatively, this
 * can be thought of as pairs of numbers in the underlying field {@code (a, b)} that have
 * componentwise addition and multiplication defined by:
 * 
 * <p>
 *   {@code (a, b) * (c, d) = (ac - bd, ad + bc)}
 * </p>
 * 
 * Note that this should not be used iteratively as it technically destroys commutivity of
 * multiplication (and is thus not a field). For once iterated version, use 
 * {@link util.math.vectorspaces.Quaternions}
 */
public class Complex<R> 
    implements 
        Field<Prod<R, R>> {

    /**
     * The unit "real" number.
     */
    public final Prod<R, R> UNIT_R;

    /**
     * The unit "imaginary" number.
     */
    public final Prod<R, R> UNIT_I;
    
    private final Field<R> FIELD;

    /**
     * Creates a complex number field 
     * 
     * @param underlyingField
     */
    public Complex(final Field<R> underlyingField) {
        this.FIELD  = underlyingField;
        this.UNIT_I = fromImaginary(FIELD.unit());
        this.UNIT_R = fromReal(FIELD.unit());
    }

    /**
     * Builds a complex number {@code a + ib} from the given real and imaginary
     * components.
     * 
     * @param a the real component
     * @param b the imaginary component
     * @return the corresponding complex value
     */
    public Prod<R, R> complex(final R a, final R b) {
        return Prod.pair(a, b);
    }

    /**
     * Extracts the real-valued part of the given complex number.
     */
    public R real(final Prod<R, R> c) {
        return c.first();
    }

    /**
     * Injects a real number into the complex numbers.
     * 
     * @param real the real number to inject
     * @return the complex number {@code a + i * 0}
     */
    public Prod<R, R> fromReal(final R real) {
        return Prod.pair(real, FIELD.zero());
    }

    /**
     * Extracts the imaginary-valued part of the given complex number.
     */
    public R imaginary(final Prod<R, R> c) {
        return c.second();
    }

    /**
     * Injects an imaginary number into the complex numbers.
     * 
     * @param imaginary the imaginary number to inject
     * @return the complex number {@code 0 + ib}
     */
    public Prod<R, R> fromImaginary(final R imaginary) {
        return Prod.pair(FIELD.zero(), imaginary);
    }

    /**
     * Converts the given complex number into a vector.
     * 
     * @param c the complex number {@code a + ib}
     * @return the vector {@code (a, b)}
     */
    public HomTuple<Two, R> toVector(final Prod<R, R> c) {
        return HomTuple.tuple(c.first(), c.second());
    }

    /**
     * Computes the complex conjugate. For a complex number {@code a + ib}, this is equivalent to
     * {@code a - ib}.
     * 
     * @param c the complex number
     * @return the complex conjugate
     */
    public Prod<R, R> conjugate(final Prod<R, R> c) {
        return Prod.mapSecond(c, FIELD::neg);
    }

    /**
     * Computes the squared distance from the origin. For a complex number {@code a + ib}, this is
     * equivalent to {@code a^2 + b^2}.
     * 
     * @param c the complex number
     * @return the squared distance to the origin
     */
    public R magnitude2(final Prod<R, R> c) {
        return real(mult(c, conjugate(c)));
    }

    @Override
    public Prod<R, R> unit() {
        return Prod.pair(FIELD.unit(), FIELD.zero());
    }

    @Override
    public Prod<R, R> mult(final Prod<R, R> r1, final Prod<R, R> r2) {
        return Prod.pair(
            FIELD.sub(FIELD.mult(r1.first(), r2.first()),  FIELD.mult(r1.second(), r2.second())),
            FIELD.sum(FIELD.mult(r1.first(), r2.second()), FIELD.mult(r1.second(), r2.first())));
    }

    @Override
    public Prod<R, R> sum(final Prod<R, R> g1, final Prod<R, R> g2) {
        return Prod.pair(
            FIELD.sum(g1.first(), g2.first()),
            FIELD.sum(g1.second(), g2.second()));
    }

    @Override
    public Prod<R, R> neg(final Prod<R, R> g) {
        return Prod.map(g, FIELD::neg, FIELD::neg);
    }

    @Override
    public Prod<R, R> zero() {
        return Prod.pair(FIELD.zero(), FIELD.zero());
    }

    @Override
    public boolean equiv(final Prod<R, R> a1, final Prod<R, R> a2) {
        return 
            FIELD.equiv(a1.first(), a2.first()) && 
            FIELD.equiv(a1.second(), a2.second());
    }

    @Override
    public Field<Prod<R, R>> underlyingField() {
        return this;
    }

    @Override
    public Prod<R, R> scale(final Prod<R, R> v, final Prod<R, R> scalar) {
        return mult(v, scalar);
    }

    @Override
    public Sum<Unit, Prod<R, R>> inv(final Prod<R, R> q) {
        return Sum.map(
            FIELD.inv(magnitude2(q)),
                __     -> Unit.unit(),
                scalar -> mult(conjugate(q), fromReal(scalar)));
    }
}
