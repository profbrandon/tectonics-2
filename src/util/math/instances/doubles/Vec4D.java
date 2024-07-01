package util.math.instances.doubles;

import java.util.List;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Four;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

public final class Vec4D extends VecD<Four> {
    
    public static final VecD<Four> INSTANCE = new Vec4D();

    public static final HomTuple<Four, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Four, Double> UNIT_X = vector(1, 0, 0, 0);
    public static final HomTuple<Four, Double> UNIT_Y = vector(0, 1, 0, 0);
    public static final HomTuple<Four, Double> UNIT_Z = vector(0, 0, 1, 0);
    public static final HomTuple<Four, Double> UNIT_W = vector(0, 0, 0, 1);

    public static HomTuple<Four, Double> vector(final double x, final double y, final double z, final double w) {
        return new HomTuple<>(OrdinalSet.fourHomo(x, y, z, w));
    }

    @Override
    public List<HomTuple<Four, Double>> basis() {
        return List.of(UNIT_X, UNIT_Y, UNIT_Z, UNIT_W);
    }

    @Override
    public List<Prod<Double, HomTuple<Four, Double>>> decompose(final HomTuple<Four, Double> v) {
        return List.of(
            Prod.pair(v.at(OrdinalSet.ZERO_4), UNIT_X),
            Prod.pair(v.at(OrdinalSet.ONE_4), UNIT_Y),
            Prod.pair(v.at(OrdinalSet.TWO_4), UNIT_Z),
            Prod.pair(v.at(OrdinalSet.THREE_4), UNIT_W));
    }

    @Override
    public Double dot(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        final HomTuple<Four, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.FOUR_SET.stream().map(prod::at).toList());
    }
}
