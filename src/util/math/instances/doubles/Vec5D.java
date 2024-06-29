package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Five;
import util.data.algebraic.HomTuple;

public final class Vec5D extends VecD<Five> {
    
    public static final VecD<Five> INSTANCE = new Vec5D();

    public static final HomTuple<Five, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Five, Double> UNIT_X = vector(1, 0, 0, 0, 0);
    public static final HomTuple<Five, Double> UNIT_Y = vector(0, 1, 0, 0, 0);
    public static final HomTuple<Five, Double> UNIT_Z = vector(0, 0, 1, 0, 0);
    public static final HomTuple<Five, Double> UNIT_W = vector(0, 0, 0, 1, 0);
    public static final HomTuple<Five, Double> UNIT_U = vector(0, 0, 0, 0, 1);

    public static HomTuple<Five, Double> vector(final double x, final double y, final double z, final double w, final double u) {
        return new HomTuple<>(OrdinalSet.fiveHomo(x, y, z, w, u));
    }

    public Double dot(final HomTuple<Five, Double> v1, final HomTuple<Five, Double> v2) {
        final HomTuple<Five, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.FIVE_SET.stream().map(prod::at).toList());
    }
}
