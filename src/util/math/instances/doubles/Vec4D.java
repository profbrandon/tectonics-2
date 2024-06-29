package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Four;
import util.data.algebraic.HomTuple;

public final class Vec4D extends VecD<Four> {
    
    public static final Vec4D INSTANCE = new Vec4D();

    public static HomTuple<Four, Double> vector(final double x, final double y, final double z, final double w) {
        return new HomTuple<>(OrdinalSet.fourHomo(x, y, z, w));
    }
}
