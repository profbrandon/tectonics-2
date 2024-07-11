package util.math.instances.doubles.vectors;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Field;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent 1-dimensional vectors over the {@link DoubleField}.
 */
public class Vec1D extends VecD<One> {

    public static final VecD<One> INSTANCE = new Vec1D();

    @Override
    public Field<Double> underlyingField() {
        return super.underlyingField();
    }

    @Override
    public Double dot(final HomTuple<One, Double> v1, final HomTuple<One, Double> v2) {
        return underlyingField().mult(extract(v1), extract(v2));
    }

    @Override
    public List<HomTuple<One, Double>> basis() {
        return DoubleField.INSTANCE.basis().stream().map(v -> HomTuple.tuple(v)).toList();
    }

    @Override
    public List<Prod<Double, HomTuple<One, Double>>> decompose(final HomTuple<One, Double> v) {
        return DoubleField.INSTANCE.decompose(extract(v)).stream().map(pair -> Prod.mapSecond(pair, x -> HomTuple.tuple(x))).toList();
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
