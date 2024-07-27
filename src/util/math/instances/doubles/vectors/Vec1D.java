package util.math.instances.doubles.vectors;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 1-dimensional vectors over the {@link DoubleField}. This is trivially
 * isomorphic to the field of doubles.
 */
public class Vec1D 
    extends 
        VecD<One> {

    public static final VecD<One> INSTANCE = new Vec1D();

    /**
     * The zero 1-dimensional double-valued vector (i.e, 0.0).
     */
    public static final HomTuple<One, Double> ZERO = INSTANCE.zero();

    /**
     * The unit 1-dimensional double-valued vector (i.e, 1.0).
     */
    public static final HomTuple<One, Double> UNIT = HomTuple.tuple(DoubleField.INSTANCE.unit());

    private Vec1D() {
        super(Ordinal.ONE_SET);
    }

    /**
     * Builds the 1-dimensional vector equivalent of the given double.
     * 
     * @param x the double value
     * @return the equivalent 1-dimensional vector
     */
    public static final HomTuple<One, Double> vector(final double x) {
        return HomTuple.tuple(x);
    }

    /**
     * Extracts the double value contained in this 1-dimensional vector.
     * 
     * @param v the vector to extract the double from
     * @return the double contained in the provided vector
     */
    public static Double extract(final HomTuple<One, Double> v) {
        Preconditions.throwIfNull(v, "v");
        return v.at(Ordinal.ZERO_1);
    }
}
