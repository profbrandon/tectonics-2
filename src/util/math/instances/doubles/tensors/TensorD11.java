package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

public class TensorD11 extends TensorD<One, One> {

    public static final TensorD<One, One> INSTANCE = new TensorD11();

    public static final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> UNIT = Prod.pair(
        new HomTuple<>(Ordinal.one(DoubleField.INSTANCE.unit())),
        new HomTuple<>(Ordinal.one(Exp.constant(DoubleField.INSTANCE.unit()))));

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> tensor,
        final HomTuple<One, Exp<Double, Double>> dualVectors,
        final HomTuple<One, Double> vectors) {

        return tensor.destroy(values -> maps -> 
            DoubleField.INSTANCE.mult(
                dualVectors.zip(values)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(Ordinal.ZERO_1),
                maps.zip(vectors)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(Ordinal.ZERO_1)));
    }

    @Override
    public List<Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>>>> decompose(
            final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> tensor) {
        
        return List.of(Prod.pair(evaluate(tensor, UNIT.second(), UNIT.first()), UNIT));
    }
    
    @Override
    public boolean equiv(
        final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> v1,
        final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> v2) {
        
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        final List<Double> components1 = decompose(v1).stream().map(pair -> pair.first()).toList();
        final List<Double> components2 = decompose(v2).stream().map(pair -> pair.first()).toList();

        for (int i = 0; i < basis().size(); ++i) {
            if (!UNDERLYING_F.equiv(components1.get(i), components2.get(i))) return false;
        }

        return true;
    }
}
