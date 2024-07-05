package util.math.instances.doubles.vectors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.Five;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

public final class Vec5D extends VecD<Five> {
    
    public static final VecD<Five> INSTANCE = new Vec5D();

    public static final HomTuple<Five, Double> ZERO   = INSTANCE.zero();
    public static final HomTuple<Five, Double> UNIT_X = vector(1, 0, 0, 0, 0);
    public static final HomTuple<Five, Double> UNIT_Y = vector(0, 1, 0, 0, 0);
    public static final HomTuple<Five, Double> UNIT_Z = vector(0, 0, 1, 0, 0);
    public static final HomTuple<Five, Double> UNIT_W = vector(0, 0, 0, 1, 0);
    public static final HomTuple<Five, Double> UNIT_U = vector(0, 0, 0, 0, 1);

    public static HomTuple<Five, Double> vector(final double x, final double y, final double z, final double w, final double u) {
        return new HomTuple<>(Ordinal.fiveHomo(x, y, z, w, u));
    }

    public static boolean equalsVector(final HomTuple<Five, Double> v1, final HomTuple<Five, Double> v2) {
        return INSTANCE.equiv(v1, v2);
    }

    @Override
    public List<HomTuple<Five, Double>> basis() {
        return List.of(UNIT_X, UNIT_Y, UNIT_Z, UNIT_W, UNIT_U);
    }

    @Override
    public List<Prod<Double, HomTuple<Five, Double>>> decompose(final HomTuple<Five, Double> v) {
        return List.of(
            Prod.pair(v.at(Ordinal.ZERO_5), UNIT_X),
            Prod.pair(v.at(Ordinal.ONE_5), UNIT_Y),
            Prod.pair(v.at(Ordinal.TWO_5), UNIT_Z),
            Prod.pair(v.at(Ordinal.THREE_5), UNIT_W),
            Prod.pair(v.at(Ordinal.FOUR_5), UNIT_U));
    }

    @Override
    public Double dot(final HomTuple<Five, Double> v1, final HomTuple<Five, Double> v2) {
        final HomTuple<Five, Double> prod = v1.zip(v2)
            .mapAll(pair -> 
                pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(Ordinal.FIVE_SET.stream().map(prod::at).toList());
    }

    @Override
    public boolean equiv(final HomTuple<Five, Double> v1, final HomTuple<Five, Double> v2) {
        return super.equalsVector(Ordinal.FIVE_SET, v1, v2);
    }
}
