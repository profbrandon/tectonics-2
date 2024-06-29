package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Three;
import util.data.algebraic.HomTuple;

public final class Vec3D extends VecD<Three> {
    
    public static final Vec3D INSTANCE = new Vec3D();

    public static HomTuple<Three, Double> vector(final double x, double y, double z) {
        return new HomTuple<>(OrdinalSet.threeHomo(x, y, z));
    }
}
