package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Two;
import util.data.algebraic.HomTuple;

public final class Vec2D extends VecD<Two> {

    public static final Vec2D INSTANCE = new Vec2D();

    public static HomTuple<Two, Double> vector(final double x, final double y) {
        return new HomTuple<>(OrdinalSet.twoHomo(x, y));
    }
}
