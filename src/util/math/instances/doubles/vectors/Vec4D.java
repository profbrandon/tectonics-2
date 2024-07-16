package util.math.instances.doubles.vectors;

import util.counting.Ordinal;
import util.counting.Cardinals.Four;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 4-dimensional vectors over the {@link DoubleField}.
 */
public final class Vec4D 
    extends 
        VecD<Four> {
    
    public static final VecD<Four> INSTANCE = new Vec4D();

    /**
     * The zero 4-dimensional vector. In normal coordinates it is (0, 0, 0, 0).
     */
    public static final HomTuple<Four, Double> ZERO   = INSTANCE.zero();

    /**
     * The unit 4-dimensional "x" vector. In normal coordinates it is (1, 0, 0, 0).
     */
    public static final HomTuple<Four, Double> UNIT_X = vector(1, 0, 0, 0);
    
    /**
     * The unit 4-dimensional "y" vector. In normal coordinates it is (0, 1, 0, 0).
     */
    public static final HomTuple<Four, Double> UNIT_Y = vector(0, 1, 0, 0);

    /**
     * The unit 4-dimensional "z" vector. In normal coordinates it is (0, 0, 1, 0).
     */
    public static final HomTuple<Four, Double> UNIT_Z = vector(0, 0, 1, 0);

    /**
     * The unit 4-dimensional "w" vector. In normal coordinates it is (0, 0, 0, 1).
     */
    public static final HomTuple<Four, Double> UNIT_W = vector(0, 0, 0, 1);

    private Vec4D() {
        super(Ordinal.FOUR_SET);
    }

    /**
     * Builds a 4-dimensional vector from the four {@link Double}s.
     * 
     * @param x the "x" coordinate of the vector
     * @param y the "y" coordinate of the vector
     * @param z the "z" coordinate of the vector
     * @param w the "w" coordinate of the vector
     * @return the vector representation of the given coordinates
     */
    public static HomTuple<Four, Double> vector(final double x, final double y, final double z, final double w) {
        return HomTuple.tuple(x, y, z, w);
    }

    /**
     * Determines if the two vectors are equivalent (up to {@link DoubleField#equiv(Double, Double)})
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return whether the two vectors are equivalent
     */
    public static boolean equalsVector(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }
}
