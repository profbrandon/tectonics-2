package util.math.instances.doubles.vectors;

import util.counting.Ordinal;
import util.counting.Cardinals.Three;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 3-dimensional vectors over the {@link DoubleField}.
 */
public final class Vec3D 
    extends 
        VecD<Three> {
    
    public static final VecD<Three> INSTANCE = new Vec3D();

    /**
     * The zero vector. In normal coordinates it is (0, 0, 0).
     */
    public static final HomTuple<Three, Double> ZERO   = INSTANCE.zero();

    /**
     * The unit "x" vector. In normal coordinates it is (1, 0, 0).
     */
    public static final HomTuple<Three, Double> UNIT_X = vector(1, 0, 0);

    /**
     * The unit "y" vector. In normal coordinates it is (0, 1, 0).
     */
    public static final HomTuple<Three, Double> UNIT_Y = vector(0, 1, 0);

    /**
     * The unit "z" vector. In normal coordinates it is (0, 0, 1).
     */
    public static final HomTuple<Three, Double> UNIT_Z = vector(0, 0, 1);

    private Vec3D() {
        super(Ordinal.THREE_SET);
    }

    /**
     * Builds a vector from the three {@link Double}s.
     * 
     * @param x the "x" component of the vector
     * @param y the "y" component of the vector
     * @param z the "z" component of the vector
     * @return the vector (a 3-tuple)
     */
    public static HomTuple<Three, Double> vector(final double x, double y, double z) {
        return HomTuple.tuple(x, y, z);
    }

    /**
     * Determines if the two vectors are equal up to {@link DoubleField#equiv(Double, Double)}.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return whether the two vectors are equal
     */
    public static boolean equalsVector(final HomTuple<Three, Double> v1, final HomTuple<Three, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }
}
