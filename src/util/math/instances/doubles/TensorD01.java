package util.math.instances.doubles;

import java.util.List;

import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.counting.Ordinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

/**
 * Class to represent the space of (0,1)-tensors over the space of doubles. This is equivalent to the
 * linear maps from Double -> Double and therefore is isomorphic to the space of doubles themselves.
 */
public class TensorD01 extends TensorD<Zero, One> {

    public static final TensorD<Zero, One> INSTANCE = new TensorD01();

    public static final Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>> UNIT = Prod.pair(
        new HomTuple<>(OrdinalSet.zero()),
        new HomTuple<>(OrdinalSet.one(Exp.asExponential(a -> DoubleField.INSTANCE.unit()))));

    @Override
    public Double evaluate(
        final Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>> tensor,
        final HomTuple<Zero, Exp<Double, Double>> dualVectors,
        final HomTuple<One, Double> vectors) {
        
        return tensor.destroy(
            invalid -> 
                maps -> 
                    maps.zip(vectors)
                        .mapAll(pair -> pair.first().apply(pair.second())).at(OrdinalSet.ZERO_1));
    }

    @Override
    public List<Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>>> basis() {
        return List.of(UNIT);
    }

    /**
     * T(1) = k * 1
     */
    @Override
    public List<Prod<Double, Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>>>> decompose(
            final Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>> tensor) {

        return List.of(Prod.pair(
            tensor.second().at(OrdinalSet.ZERO_1).apply(DoubleField.INSTANCE.unit()),
            UNIT));
    }
}
