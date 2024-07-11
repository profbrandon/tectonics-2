package util.math.instances.doubles.linear;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.vectors.Vec2D;

public class Linear2D extends LinearD<Two, Two> {
    
    public static final LinearD<Two, Two> INSTANCE = new Linear2D();

    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_1_1 =
        Exp.asExponential(tuple -> HomTuple.tuple(tuple.at(Ordinal.ZERO_2), DoubleField.INSTANCE.zero()));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_1_2 =
        Exp.asExponential(tuple -> HomTuple.tuple(tuple.at(Ordinal.ONE_2), DoubleField.INSTANCE.zero()));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_2_1 =
        Exp.asExponential(tuple -> HomTuple.tuple(DoubleField.INSTANCE.zero(), tuple.at(Ordinal.ZERO_2)));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> UNIT_2_2 =
        Exp.asExponential(tuple -> HomTuple.tuple( DoubleField.INSTANCE.zero(), tuple.at(Ordinal.ONE_2)));

    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> ZERO     = INSTANCE.zero();
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> IDENTITY = INSTANCE.sumAll(List.of(UNIT_1_1, UNIT_2_2));
    public static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> EXCHANGE = INSTANCE.sumAll(List.of(UNIT_1_2, UNIT_2_1));

    public Linear2D() {
        super(Vec2D.INSTANCE, Vec2D.INSTANCE);
    }

    public static Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> asLinearMap(
        final double m11, 
        final double m12, 
        final double m21, 
        final double m22) {

        return INSTANCE.sumAll(List.of(
            INSTANCE.scale(UNIT_1_1, m11),
            INSTANCE.scale(UNIT_1_2, m12),
            INSTANCE.scale(UNIT_2_1, m21),
            INSTANCE.scale(UNIT_2_2, m22)
        ));
    }

    @Override
    public List<Exp<HomTuple<Two, Double>, HomTuple<Two, Double>>> basis() {
        return List.of(UNIT_1_1, UNIT_1_2, UNIT_2_1, UNIT_2_2);
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<Two, Double>, HomTuple<Two, Double>>>> decompose(
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> linear) {
        
        final HomTuple<Two, Double> v1 = linear.apply(Vec2D.UNIT_X);
        final HomTuple<Two, Double> v2 = linear.apply(Vec2D.UNIT_Y);

        return List.of(
            Prod.pair(v1.at(Ordinal.ZERO_2), UNIT_1_1),
            Prod.pair(v2.at(Ordinal.ZERO_2), UNIT_1_2),
            Prod.pair(v1.at(Ordinal.ONE_2), UNIT_2_1),
            Prod.pair(v2.at(Ordinal.ONE_2), UNIT_2_2));
    }

    @Override
    public boolean equiv(
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> a1,
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> a2) {
        
        final List<Double> components1 = decompose(a1).stream().map(Prod::first).toList();
        final List<Double> components2 = decompose(a2).stream().map(Prod::first).toList();

        if (components1.size() != components2.size()) {
            throw new IllegalStateException("Somehow tested equality between two linear maps of different component length");
        }

        for (int i = 0; i < components1.size(); ++i) {
            if (!DoubleField.INSTANCE.equiv(components1.get(i), components2.get(i))) return false;
        }

        return true;
    }
}
