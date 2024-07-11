package util.math.instances.doubles.vectors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Field;
import util.math.instances.doubles.DoubleField;

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
    public List<Prod<Double, HomTuple<One, Double>>> decompose(HomTuple<One, Double> v) {
        return DoubleField.INSTANCE.decompose(extract(v)).stream().map(pair -> Prod.mapSecond(pair, x -> HomTuple.tuple(x))).toList();
    }

    public static Double extract(final HomTuple<One, Double> v) {
        return v.at(Ordinal.ZERO_1);
    }
}
