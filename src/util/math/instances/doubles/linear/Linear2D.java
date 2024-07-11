package util.math.instances.doubles.linear;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Ring;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.vectors.Vec2D;

/**
 * Class to represent the 2-dimensional linear endomorphisms on {@link Vec2D}.
 */
public class Linear2D extends LinearD<Two, Two> implements Ring<Exp<HomTuple<Two, Double>, HomTuple<Two, Double>>> {
    
    public static final Linear2D INSTANCE = new Linear2D();

    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_1_1 =
        Exp.asExponential(tuple -> HomTuple.tuple(tuple.at(Ordinal.ZERO_2), DoubleField.INSTANCE.zero()));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_1_2 =
        Exp.asExponential(tuple -> HomTuple.tuple(tuple.at(Ordinal.ONE_2), DoubleField.INSTANCE.zero()));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_2_1 =
        Exp.asExponential(tuple -> HomTuple.tuple(DoubleField.INSTANCE.zero(), tuple.at(Ordinal.ZERO_2)));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_2_2 =
        Exp.asExponential(tuple -> HomTuple.tuple( DoubleField.INSTANCE.zero(), tuple.at(Ordinal.ONE_2)));

    /**
     * The linear map sending everything to zero.
     */
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> ZERO = INSTANCE.zero();

    /**
     * The identity linear map sending each vector to itself
     */
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> IDENTITY = INSTANCE.sumAll(List.of(UNIT_1_1, UNIT_2_2));

    /**
     * The exchange linear map, flipping a 2-dimensional vector around the {@code x = y} axis.
     */
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> EXCHANGE = INSTANCE.sumAll(List.of(UNIT_1_2, UNIT_2_1));

    private Linear2D() {
        super(Vec2D.INSTANCE, Vec2D.INSTANCE);
    }

    /**
     * Builds a linear map from a matrix of numbers.
     * 
     * @param m11 the entry in the first row and first column
     * @param m12 the entry in the first row and second column
     * @param m21 the entry in the second row and first column
     * @param m22 the entry in the second row and second column
     * @return a linear map representation of the matrix
     */
    public static Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> asLinearMap(
        final double m11, 
        final double m12, 
        final double m21, 
        final double m22) {

        Preconditions.throwIfNull(m11, "m11");
        Preconditions.throwIfNull(m12, "m12");
        Preconditions.throwIfNull(m21, "m21");
        Preconditions.throwIfNull(m22, "m22");

        return INSTANCE.sumAll(List.of(
            INSTANCE.scale(UNIT_1_1, m11),
            INSTANCE.scale(UNIT_1_2, m12),
            INSTANCE.scale(UNIT_2_1, m21),
            INSTANCE.scale(UNIT_2_2, m22)
        ));
    }

    /**
     * Builds a linear transformation that rotates vectors by the given angle.
     * 
     * @param radians the angle to rotate vectors
     * @return a linear transformation that rotates vectors
     */
    public static Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> getRotation(final double radians) {
        Preconditions.throwIfNull(radians, "radians");
        return asLinearMap(
            Math.cos(radians), - Math.sin(radians),
            Math.sin(radians), Math.cos(radians));
    }

    /**
     * Builds a linear transformation that scales vectors by the given scalar.
     * 
     * @param scalar the value to scale vectors by
     * @return a linear transformation that scales vectors
     */
    public static Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> getScale(final double scalar) {
        Preconditions.throwIfNull(scalar, "scalar");
        return INSTANCE.scale(IDENTITY, scalar);
    }

    @Override
    public List<Exp<HomTuple<Two, Double>, HomTuple<Two, Double>>> basis() {
        return List.of(UNIT_1_1, UNIT_1_2, UNIT_2_1, UNIT_2_2);
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<Two, Double>, HomTuple<Two, Double>>>> decompose(
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> linear) {
        
        final HomTuple<Two, Double> v1 = linear.apply(Vec2D.UNIT_X);
        final HomTuple<Two, Double> v2 = linear.apply(Vec2D.UNIT_Y);

        return List.of(
            Prod.pair(v1.at(Ordinal.ZERO_2), UNIT_1_1),
            Prod.pair(v2.at(Ordinal.ZERO_2), UNIT_1_2),
            Prod.pair(v1.at(Ordinal.ONE_2), UNIT_2_1),
            Prod.pair(v2.at(Ordinal.ONE_2), UNIT_2_2));
    }

    @Override
    public Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> unit() {
        return IDENTITY;
    }

    @Override
    public Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> mult(
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> r1,
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> r2) {
        
        return r1.after(r2);
    }
}
