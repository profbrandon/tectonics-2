package util.math.instances.doubles;

import java.util.function.Function;

import util.Functional;
import util.counting.Cardinals.Three;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.Quaternions;

public class QuaternionsD
    extends
        Quaternions<Double> {
    
    public static final Quaternions<Double> INSTANCE = new QuaternionsD();

    private QuaternionsD() {
        super(DoubleField.INSTANCE);
    }

    /**
     * Computes the norm, i.e., the distance to the origin.
     * 
     * @param q the quaternion
     * @return the distance to the origin
     */
    public static double norm(final Prod<Prod<Double, Double>, Prod<Double, Double>> q) {
        return Math.sqrt(INSTANCE.magnitude2(q));
    }

    /**
     * Creates a unit quaternion from the given quaternion (but is the identity on zero).
     * 
     * @param q the quaternion to normalize
     * @return either 0 or a quaternion with norm 1
     */
    public static Prod<Prod<Double, Double>, Prod<Double, Double>> 
        normalize(
            final Prod<Prod<Double, Double>, Prod<Double, Double>> q) {

        return INSTANCE.underlyingField().inv(norm(q)).match(
            __          -> INSTANCE.zero(),
            recipLength -> INSTANCE.scale(q, recipLength));
    }

    /**
     * Creates a rotation function around the given axis by the given angle.
     * 
     * @param axis the axis to rotate around
     * @param angle the angle to rotate through
     * @return a function that rotates vectors around the given axis
     */
    public static Function<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        rotation(
            final HomTuple<Three, Double> axis, final double angle) {

        return 
            vector -> 
                Functional.let(
                    INSTANCE.sum(
                        INSTANCE.fromReal(Math.cos(angle / 2.0)), 
                        INSTANCE.scale(INSTANCE.fromVector(axis), Math.sin(angle / 2.0))), 
                    qAxis ->
                        Functional.let(
                            normalize(qAxis), 
                            unitAxis -> 
                                Functional.let(INSTANCE.fromVector(vector), qVector -> 
                                Functional.let(INSTANCE.inv(unitAxis), temp -> temp.match(
                                    __ -> vector,
                                    unitAxisInv -> 
                                        INSTANCE.vectorPart(
                                            INSTANCE.mult(unitAxis, INSTANCE.mult(qVector, unitAxisInv))))))));
    }
}
