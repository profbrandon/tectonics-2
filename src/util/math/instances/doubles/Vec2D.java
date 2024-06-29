package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.Two;
import util.data.algebraic.HomTuple;

public final class Vec2D extends VecD<Two> {

    public static final VecD<Two> INSTANCE = new Vec2D();

    public static final HomTuple<Two, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Two, Double> UNIT_X = vector(1, 0);
    public static final HomTuple<Two, Double> UNIT_Y = vector(0, 1);

    public static HomTuple<Two, Double> vector(final double x, final double y) {
        return new HomTuple<>(OrdinalSet.twoHomo(x, y));
    }

    public static boolean equalsVector(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        return INSTANCE.equalsVector(OrdinalSet.TWO_SET, v1, v2);
    }

    @Override
    public Double dot(final HomTuple<Two, Double> v1, final HomTuple<Two, Double> v2) {
        final HomTuple<Two, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(OrdinalSet.TWO_SET.stream().map(prod::at).toList());
    }
}
