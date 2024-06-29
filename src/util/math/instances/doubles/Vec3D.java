package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Three;
import util.data.algebraic.HomTuple;

public final class Vec3D extends VecD<Three> {
    
    public static final VecD<Three> INSTANCE = new Vec3D();

    public static final HomTuple<Three, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Three, Double> UNIT_X = vector(1, 0, 0);
    public static final HomTuple<Three, Double> UNIT_Y = vector(0, 1, 0);
    public static final HomTuple<Three, Double> UNIT_Z = vector(0, 0, 1);

    public static HomTuple<Three, Double> vector(final double x, double y, double z) {
        return new HomTuple<>(OrdinalSet.threeHomo(x, y, z));
    }

    public static boolean equalsVector(final HomTuple<Three, Double> v1, final HomTuple<Three, Double> v2) {
        return INSTANCE.equalsVector(OrdinalSet.THREE_SET, v1, v2);
    }

    public Double dot(final HomTuple<Three, Double> v1, final HomTuple<Three, Double> v2) {
        final HomTuple<Three, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.THREE_SET.stream().map(prod::at).toList());
    }
}
