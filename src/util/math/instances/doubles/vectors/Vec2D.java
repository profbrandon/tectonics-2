package util.math.instances.doubles.vectors;

import java.util.List;

import util.Preconditions;
import util.counting.OrdinalSet;
import util.counting.Ordinals.Two;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

public final class Vec2D extends VecD<Two> {

    public static final VecD<Two> INSTANCE = new Vec2D();

    public static final HomTuple<Two, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Two, Double> UNIT_X = vector(1, 0);
    public static final HomTuple<Two, Double> UNIT_Y = vector(0, 1);

    public static HomTuple<Two, Double> vector(final double x, final double y) {
        Preconditions.throwIfNull(x, "x");
        Preconditions.throwIfNull(y, "y");
        return new HomTuple<>(OrdinalSet.twoHomo(x, y));
    }

    public static boolean equalsVector(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }

    @Override
    public List<HomTuple<Two, Double>> basis() {
        return List.of(UNIT_X, UNIT_Y);
    }

    @Override
    public List<Prod<Double, HomTuple<Two, Double>>> decompose(final HomTuple<Two, Double> v) {
        Preconditions.throwIfNull(v, "v");
        return List.of(
            Prod.pair(v.at(OrdinalSet.ZERO_2), UNIT_X),
            Prod.pair(v.at(OrdinalSet.ONE_2), UNIT_Y));
    }

    @Override
    public Double dot(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        final HomTuple<Two, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.TWO_SET.stream().map(prod::at).toList());
    }

    @Override
    public boolean equiv(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        return super.equalsVector(OrdinalSet.TWO_SET, v1, v2);
    }
}
