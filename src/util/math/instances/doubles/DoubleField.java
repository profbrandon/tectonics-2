package util.math.instances.doubles;

import java.util.List;

import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;
import util.math.vectorspaces.finite.FiniteVectorSpace;

/**
 * Class to represent a mathematical {@link Field} of {@link Double} values. Equality between
 * double values is considered up to one-onebillionth.
 */
public final class DoubleField 
    implements 
        Field<Double>, 
        FiniteVectorSpace<Double, Double> {

    private static final Double TOLERANCE = 1e-9;

    public static final DoubleField INSTANCE = new DoubleField();

    /**
     * The zero double (0.0).
     */
    public static final Double ZERO = INSTANCE.zero();

    /**
     * The unit double (1.0).
     */
    public static final Double UNIT = INSTANCE.unit();

    private DoubleField() {};

    @Override
    public Field<Double> underlyingField() {
        return INSTANCE;
    }

    @Override
    public List<Double> basis() {
        return List.of(unit());
    }

    @Override
    public List<Prod<Double, Double>> decompose(final Double v) {
        return List.of(Prod.pair(v, unit()));
    }

    @Override
    public Double zero() {
        return 0.0;
    }

    @Override
    public Double unit() {
        return 1.0;
    }

    @Override
    public Double sum(final Double q1, final Double q2) {
        return q1 + q2;
    }

    @Override
    public Double neg(final Double q) {
        return -q;
    }

    @Override
    public Double mult(final Double q1, final Double q2) {
        return q1 * q2;
    }

    @Override
    public Sum<Unit, Double> inv(final Double q) {
        if (q.equals(this.zero())) {
            return Sum.left(Unit.unit());
        } else {
            return Sum.right(1.0 / q);
        }
    }

    @Override
    public Double scale(final Double v, final Double scalar) {
        return this.mult(v, scalar);
    }   

    @Override
    public boolean equiv(final Double a1, final Double a2) {
        final double differance = a1.doubleValue() - a2.doubleValue();
        return -TOLERANCE < differance && differance < TOLERANCE;
    }
}
