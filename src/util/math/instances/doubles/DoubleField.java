package util.math.instances.doubles;

import java.util.Arrays;
import java.util.List;

import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;
import util.math.vectorspaces.FiniteVectorSpace;

public final class DoubleField implements Field<Double>, FiniteVectorSpace<Double, Double> {

    private static final Double TOLERANCE = 1e-10;

    public static final DoubleField INSTANCE = new DoubleField();

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

    public Double sumAll(final Double...vs) {
        return sumAll(Arrays.asList(vs));
    }

    @Override
    public boolean equiv(final Double a1, final Double a2) {
        final double differance = a1.doubleValue() - a2.doubleValue();
        return -TOLERANCE < differance && differance < TOLERANCE;
    }
}
