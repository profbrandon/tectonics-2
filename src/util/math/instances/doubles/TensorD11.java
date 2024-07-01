package util.math.instances.doubles;

import java.util.List;

import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

public class TensorD11 extends TensorD<One, One> {

    public static final TensorD<One, One> INSTANCE = new TensorD11();

    public static final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> UNIT = Prod.pair(
        new HomTuple<>(OrdinalSet.one(DoubleField.INSTANCE.unit())),
        new HomTuple<>(OrdinalSet.one(Exp.constant(DoubleField.INSTANCE.unit()))));

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, Double>, HomTuple<One, Exp<Double, Double>>> tensor,
        final HomTuple<One, Exp<Double, Double>> dualVectors,
        final HomTuple<One, Double> vectors) {

        return tensor.destroy(values -> maps -> 
            DoubleField.INSTANCE.mult(
                dualVectors.zip(values)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(OrdinalSet.ZERO_1),
                maps.zip(vectors)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(OrdinalSet.ZERO_1)));
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
    
}
