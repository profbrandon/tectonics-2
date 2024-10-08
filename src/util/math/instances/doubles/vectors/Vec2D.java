package util.math.instances.doubles.vectors;

import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 2-dimensional vectors over the {@link DoubleField}.
 */
public final class Vec2D 
    extends 
        VecD<Two> {

    public static final VecD<Two> INSTANCE = new Vec2D();

    /**
     * The zero vector. In normal coordinates it is (0, 0).
     */
    public static final HomTuple<Two, Double> ZERO   = INSTANCE.zero();

    /**
     * The unit "x" vector. In normal coordinates it is (1, 0).
     */
    public static final HomTuple<Two, Double> UNIT_X = vector(1, 0);

    /**
     * The unit "y" vector. In normal coordinates it is (0, 1).
     */
    public static final HomTuple<Two, Double> UNIT_Y = vector(0, 1);

    private Vec2D() {
        super(Ordinal.TWO_SET);
    }

    /**
     * Builds a vector from the two {@link Double}s.
     * 
     * @param x the "x" component of the vector
     * @param y the "y" component of the vector
     * @return the vector (a 2-tuple)
     */
    public static HomTuple<Two, Double> vector(final double x, final double y) {
        return HomTuple.tuple(x, y);
    }

    /**
     * Extracts the x coordinate of a vector.
     */
    public static double x(final HomTuple<Two, Double> vector) {
        return vector.at(Ordinal.ZERO_2);
    }

    /**
     * Extracts the y coordinate of a vector.
     */
    public static double y(final HomTuple<Two, Double> vector) {
        return vector.at(Ordinal.ONE_2);
    }

    /**
     * Computes the angle between the two vectors (first to second, counterclockwise).
     * 
     * @param v1 the starting vector (angle = 0.0 radians)
     * @param v2 the second vector
     * @return the angle of the second vector relative to the first
     */
    public static double angle(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        final HomTuple<Two, Double> norm1 = INSTANCE.normalize(v1);
        final HomTuple<Two, Double> norm2 = INSTANCE.normalize(v2);

        final double a = Math.acos(INSTANCE.dot(norm1, norm2));
        final double w = wedge(norm1, norm2);

        if (w > 0) return a > 0 ? a : Math.PI + a;
        else return a > 0 ? 2 * Math.PI - a : Math.PI - a;
    }

    /**
     * Computes the coefficient on the wedge product between the two vectors.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the wedge product's coefficient
     */
    public static double wedge(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        return x(v1) * y(v2) - x(v2) * y(v1);
    }

    /**
     * Determines if the two vectors are equal up to {@link DoubleField#equiv(Double, Double)}.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return whether the two vectors are equal
     */
    public static boolean equalsVector(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }
}
