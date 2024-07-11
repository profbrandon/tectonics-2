package util.math.instances.doubles.vectors;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.Three;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 3-dimensional vectors over the {@link DoubleField}.
 */
public final class Vec3D extends VecD<Three> {
    
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

    /**
     * Builds a vector from the three {@link Double}s.
     * 
     * @param x the "x" component of the vector
     * @param y the "y" component of the vector
     * @param z the "z" component of the vector
     * @return the vector (a 3-tuple)
     */
    public static HomTuple<Three, Double> vector(final double x, double y, double z) {
        Preconditions.throwIfNull(x, "x");
        Preconditions.throwIfNull(y, "y");
        Preconditions.throwIfNull(z, "z");
        return new HomTuple<>(Ordinal.threeHomo(x, y, z));
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

    @Override
    public List<HomTuple<Three, Double>> basis() {
        return List.of(UNIT_X, UNIT_Y, UNIT_Z);
    }

    @Override
    public List<Prod<Double, HomTuple<Three, Double>>> decompose(final HomTuple<Three, Double> v) {
        Preconditions.throwIfNull(v, "v");
        return List.of(
            Prod.pair(v.at(Ordinal.ZERO_3), UNIT_X),
            Prod.pair(v.at(Ordinal.ONE_3), UNIT_Y),
            Prod.pair(v.at(Ordinal.TWO_3), UNIT_Z));
    }

    @Override
    public Double dot(final HomTuple<Three, Double> v1, final HomTuple<Three, Double> v2) {
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        final HomTuple<Three, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(Ordinal.THREE_SET.stream().map(prod::at).toList());
    }

    @Override
    public boolean equiv(final HomTuple<Three, Double> v1, final HomTuple<Three, Double> v2) {
        return super.equalsVector(Ordinal.THREE_SET, v1, v2);
    }
}
