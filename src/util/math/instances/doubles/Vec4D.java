package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Four;
import util.data.algebraic.HomTuple;

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

    public Double dot(final HomTuple<Four, Double> v1, final HomTuple<Four, Double> v2) {
        final HomTuple<Four, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.FOUR_SET.stream().map(prod::at).toList());
    }
}
