package util.math.instances.doubles;

import java.util.Arrays;
import java.util.Collection;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;
import util.math.VectorSpace;

public final class DoubleField implements Field<Double>, VectorSpace<Double, Double> {

    public static final DoubleField INSTANCE = new DoubleField();

    private DoubleField() {};

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

    public Double sumAll(final Collection<Double> vs) {
        if (vs.size() == 0) return zero();
        else {
            double accumulator = zero();

            for (final double v : vs) {
                accumulator = sum(accumulator, v);
            }

            return accumulator;
        }
    }
}
