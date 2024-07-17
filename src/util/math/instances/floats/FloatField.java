package util.math.instances.floats;

import java.util.List;

import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.Field;
import util.math.fields.SubField;
import util.math.instances.doubles.DoubleField;
import util.math.vectorspaces.finite.FiniteVectorSpace;

/**
 * Class to represent a mathematical {@link Field} of {@link Float} values. Equality between
 * float values is considered up to one-onemillionth.
 */
public class FloatField
    implements
        SubField<Float, Double, DoubleField>,
        FiniteVectorSpace<Float, Float> {

    private static final Double TOLERANCE = 1e-6;

    public static final FloatField INSTANCE = new FloatField();

    /**
     * The zero float (0.0f).
     */
    public static final Float ZERO = INSTANCE.zero();

    /**
     * The unit float (1.0f).
     */
    public static final Float UNIT = INSTANCE.unit();

    private FloatField() {};

    @Override
    public Field<Float> underlyingField() {
        return INSTANCE;
    }

    @Override
    public List<Float> basis() {
        return List.of(unit());
    }

    @Override
    public List<Prod<Float, Float>> decompose(final Float v) {
        return List.of(Prod.pair(v, unit()));
    }

    @Override
    public Float zero() {
        return 0.0f;
    }

    @Override
    public Float unit() {
        return 1.0f;
    }

    @Override
    public Float sum(final Float q1, final Float q2) {
        return q1 + q2;
    }

    @Override
    public Float neg(final Float q) {
        return -q;
    }

    @Override
    public Float mult(final Float q1, final Float q2) {
        return q1 * q2;
    }

    @Override
    public Sum<Unit, Float> inv(final Float q) {
        if (q.equals(this.zero())) {
            return Sum.left(Unit.unit());
        } else {
            return Sum.right(1.0f / q);
        }
    }

    @Override
    public Float scale(final Float v, final Float scalar) {
        return mult(v, scalar);
    }   

    @Override
    public boolean equiv(final Float a1, final Float a2) {
        final float differance = a1.floatValue() - a2.floatValue();
        return -TOLERANCE < differance && differance < TOLERANCE;
    }

    @Override
    public Double embedField(final Float f1) {
        return f1.doubleValue();
    }
}
