package util.math.instances.doubles.linear;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.vectors.Vec1D;

public class Linear1D extends LinearD<One, One> {

    public static final LinearD<One, One> INSTANCE = new Linear1D();

    public static final Exp<HomTuple<One, Double>, HomTuple<One, Double>> UNIT = 
        Exp.constant(HomTuple.tuple(INSTANCE.underlyingField().unit()));
    
    private Linear1D() {
        super(Vec1D.INSTANCE, Vec1D.INSTANCE);
    }

    @Override
    public List<Exp<HomTuple<One, Double>, HomTuple<One, Double>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<One, Double>, HomTuple<One, Double>>>> decompose(
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> v) {
        
        return List.of(Prod.pair(v.apply(HomTuple.tuple(underlyingField().unit())).at(Ordinal.ZERO_1), UNIT));
    }

    @Override
    public boolean equiv(
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> a1,
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> a2) {
        
        return DoubleField.INSTANCE.equiv(decompose(a1).get(0).first(), decompose(a2).get(0).first());
    }    
}
