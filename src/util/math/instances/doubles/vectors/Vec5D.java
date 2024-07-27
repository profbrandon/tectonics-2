package util.math.instances.doubles.vectors;

import util.counting.Ordinal;
import util.counting.Cardinals.Five;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 5-dimensional vectors over the {@link DoubleField}.
 */
public final class Vec5D 
    extends 
        VecD<Five> {
    
    public static final VecD<Five> INSTANCE = new Vec5D();

    /**
     * The zero 5-dimensional vector. In normal coordinates it is (0, 0, 0, 0, 0).
     */
    public static final HomTuple<Five, Double> ZERO   = INSTANCE.zero();

    /**
     * The unit 5-dimensional "x" vector. In normal coordinates it is (1, 0, 0, 0, 0).
     */
    public static final HomTuple<Five, Double> UNIT_X = vector(1, 0, 0, 0, 0);

    /**
     * The unit 5-dimensional "y" vector. In normal coordinates it is (0, 1, 0, 0, 0).
     */
    public static final HomTuple<Five, Double> UNIT_Y = vector(0, 1, 0, 0, 0);

    /**
     * The unit 5-dimensional "z" vector. In normal coordinates it is (0, 0, 1, 0, 0).
     */
    public static final HomTuple<Five, Double> UNIT_Z = vector(0, 0, 1, 0, 0);

    /**
     * The unit 5-dimensional "w" vector. In normal coordinates it is (0, 0, 0, 1, 0).
     */
    public static final HomTuple<Five, Double> UNIT_W = vector(0, 0, 0, 1, 0);

    /**
     * The unit 5-dimensional "u" vector. In normal coordinates it is (0, 0, 0, 0, 1).
     */
    public static final HomTuple<Five, Double> UNIT_U = vector(0, 0, 0, 0, 1);

    private Vec5D() {
        super(Ordinal.FIVE_SET);
    }

    /**
     * Builds a 5-dimensional vector from the five {@link Double}s (x, y, z, w, u).
     * 
     * @param x the "x" coordinate of the vector
     * @param y the "y" coordinate of the vector
     * @param z the "z" coordinate of the vector
     * @param w the "w" coordinate of the vector
     * @param u the "u" coordinate of the vector
     * @return the vector representation of the given coordinates
     */
    public static HomTuple<Five, Double> vector(final double x, final double y, final double z, final double w, final double u) {
        return HomTuple.tuple(x, y, z, w, u);
    }

    /**
     * Determines if the two vectors are equivalent (up to {@link DoubleField#equiv(Double, Double)})
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return whether the two vectors are equivalent
     */
    public static boolean equalsVector(final HomTuple<Five, Double> v1, final HomTuple<Five, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }
}
