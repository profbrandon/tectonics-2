package util.math.instances.doubles.vectors;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.Four;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

public final class Vec4D extends VecD<Four> {
    
    public static final VecD<Four> INSTANCE = new Vec4D();

    public static final HomTuple<Four, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Four, Double> UNIT_X = vector(1, 0, 0, 0);
    public static final HomTuple<Four, Double> UNIT_Y = vector(0, 1, 0, 0);
    public static final HomTuple<Four, Double> UNIT_Z = vector(0, 0, 1, 0);
    public static final HomTuple<Four, Double> UNIT_W = vector(0, 0, 0, 1);

    public static HomTuple<Four, Double> vector(final double x, final double y, final double z, final double w) {
        return new HomTuple<>(Ordinal.fourHomo(x, y, z, w));
    }

    public static boolean equalsVector(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }

    @Override
    public List<HomTuple<Four, Double>> basis() {
        return List.of(UNIT_X, UNIT_Y, UNIT_Z, UNIT_W);
    }

    @Override
    public List<Prod<Double, HomTuple<Four, Double>>> decompose(final HomTuple<Four, Double> v) {
        return List.of(
            Prod.pair(v.at(Ordinal.ZERO_4), UNIT_X),
            Prod.pair(v.at(Ordinal.ONE_4), UNIT_Y),
            Prod.pair(v.at(Ordinal.TWO_4), UNIT_Z),
            Prod.pair(v.at(Ordinal.THREE_4), UNIT_W));
    }

    @Override
    public Double dot(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        final HomTuple<Four, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(Ordinal.FOUR_SET.stream().map(prod::at).toList());
    }

    @Override
    public boolean equiv(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        return super.equalsVector(Ordinal.FOUR_SET, v1, v2);
    }
}
