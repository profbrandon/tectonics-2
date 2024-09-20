package util.math.instances.doubles.linear;

import java.util.List;

import util.Preconditions;
import util.counting.Cardinals.Three;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Ring;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.QuaternionsD;
import util.math.instances.doubles.vectors.Vec3D;

/**
 * A class to represent the space of linear transformations from the 3-dimensional space of double-vectors
 * to itself.
 */
public class Linear3D
    extends 
        LinearD<Three, Three>
    implements
        Ring<Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>> {

    public static final Linear3D INSTANCE = new Linear3D();

    /**
     * The linear transformation equivalent to the matrix [ 1 0 0, 0 0 0, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        UNIT_1_1 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                tuple.at(Ordinal.ZERO_3), 
                DoubleField.INSTANCE.zero(), 
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 1 0, 0 0 0, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_1_2 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                tuple.at(Ordinal.ONE_3), 
                DoubleField.INSTANCE.zero(),
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 1, 0 0 0, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_1_3 = Exp.asExponential(tuple ->
            HomTuple.tuple(
                tuple.at(Ordinal.TWO_3),
                DoubleField.INSTANCE.zero(),
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 1 0 0, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        UNIT_2_1 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.ZERO_3), 
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 0 1 0, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_2_2 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.ONE_3), 
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 0 0 1, 0 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_2_3 = Exp.asExponential(tuple ->
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.TWO_3),
                DoubleField.INSTANCE.zero()));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 0 0 0, 1 0 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        UNIT_3_1 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(), 
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.ZERO_3)));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 0 0 0, 0 1 0 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_3_2 = Exp.asExponential(tuple -> 
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(),
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.ONE_3)));

    /**
     * The linear transformation equivalent to the matrix [ 0 0 0, 0 0 0, 0 0 1 ].
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        UNIT_3_3 = Exp.asExponential(tuple ->
            HomTuple.tuple(
                DoubleField.INSTANCE.zero(),
                DoubleField.INSTANCE.zero(),
                tuple.at(Ordinal.TWO_3)));

    /**
     * The linear map sending everything to zero.
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        ZERO = INSTANCE.zero();

    /**
     * The identity linear map sending each vector to itself.
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        IDENTITY = INSTANCE.sumAll(List.of(UNIT_1_1, UNIT_2_2, UNIT_3_3));

    /**
     * The exchange linear map.
     */
    public static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        EXCHANGE = INSTANCE.sumAll(List.of(UNIT_1_3, UNIT_2_2, UNIT_3_1));

    private Linear3D() {
        super(Vec3D.INSTANCE, Vec3D.INSTANCE);
    }

    /**
     * Builds a linear map from a matrix of numbers.
     * 
     * @param m11 the entry in the first row and first column
     * @param m12 the entry in the first row and second column
     * @param m13 the entry in the first row and third column
     * @param m21 the entry in the second row and first column
     * @param m22 the entry in the second row and second column
     * @param m23 the entry in the second row and third column
     * @param m31 the entry in the third row and first column
     * @param m32 the entry in the third row and second column
     * @param m33 the entry in the third row and third column
     * @return a linear map representation of the matrix
     */
    public static Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        asLinearMap(
            final double m11, 
            final double m12, 
            final double m13,
            final double m21, 
            final double m22,
            final double m23,
            final double m31,
            final double m32,
            final double m33) {

        Preconditions.throwIfNull(m11, "m11");
        Preconditions.throwIfNull(m12, "m12");
        Preconditions.throwIfNull(m13, "m13");
        Preconditions.throwIfNull(m21, "m21");
        Preconditions.throwIfNull(m22, "m22");
        Preconditions.throwIfNull(m23, "m23");
        Preconditions.throwIfNull(m31, "m31");
        Preconditions.throwIfNull(m32, "m32");
        Preconditions.throwIfNull(m33, "m33");

        return INSTANCE.sumAll(List.of(
            INSTANCE.scale(UNIT_1_1, m11),
            INSTANCE.scale(UNIT_1_2, m12),
            INSTANCE.scale(UNIT_1_3, m13),
            INSTANCE.scale(UNIT_2_1, m21),
            INSTANCE.scale(UNIT_2_2, m22),
            INSTANCE.scale(UNIT_2_3, m23),
            INSTANCE.scale(UNIT_3_1, m31),
            INSTANCE.scale(UNIT_3_2, m32),
            INSTANCE.scale(UNIT_3_3, m33)
        ));
    }

    /**
     * Builds a linear transformation that rotates 3-dimensional vectors by the given angle around
     * the given axis.
     * 
     * @param axis the vector axis to rotate around
     * @param radians the angle to rotate vectors
     * @return a linear transformation that rotates vectors
     */
    public static Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        getRotation(
            final HomTuple<Three, Double> axis, 
            final double radians) {

        return Exp.asExponential(QuaternionsD.rotation(axis, radians));
    }

    /**
     * Builds a linear transformation that scales 3-dimensionsal vectors by the given scalar quantity.
     * 
     * @param scalar the scalar quantity to stretch the vector by
     * @return a linear transformation that stretches the vector
     */
    public static Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>
        getScale(
            final double scalar) {

        return INSTANCE.scale(IDENTITY, scalar);
    }

    @Override
    public List<Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>> basis() {
        return List.of(
            UNIT_1_1,
            UNIT_1_2,
            UNIT_1_3,
            UNIT_2_1,
            UNIT_2_2,
            UNIT_2_3,
            UNIT_3_1,
            UNIT_3_2,
            UNIT_3_3
        );
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<Three, Double>, HomTuple<Three, Double>>>> 
        decompose(
            final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> linear) {

        Preconditions.throwIfNull(linear, "linear");
        
        final HomTuple<Three, Double> v1 = linear.apply(Vec3D.UNIT_X);
        final HomTuple<Three, Double> v2 = linear.apply(Vec3D.UNIT_Y);
        final HomTuple<Three, Double> v3 = linear.apply(Vec3D.UNIT_Z);

        return List.of(
            Prod.pair(v1.at(Ordinal.ZERO_3), UNIT_1_1),
            Prod.pair(v2.at(Ordinal.ZERO_3), UNIT_1_2),
            Prod.pair(v3.at(Ordinal.ZERO_3), UNIT_1_3),
            Prod.pair(v1.at(Ordinal.ONE_3), UNIT_2_1),
            Prod.pair(v2.at(Ordinal.ONE_3), UNIT_2_2),
            Prod.pair(v3.at(Ordinal.ONE_3), UNIT_2_3),
            Prod.pair(v1.at(Ordinal.TWO_3), UNIT_3_1),
            Prod.pair(v2.at(Ordinal.TWO_3), UNIT_3_2),
            Prod.pair(v3.at(Ordinal.TWO_3), UNIT_3_3));
    }

    @Override
    public Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> unit() {
        return IDENTITY;
    }

    @Override
    public Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        mult(
            final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> r1,
            final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> r2) {

        return LinearD.compose(r2, r1);
    }
}
