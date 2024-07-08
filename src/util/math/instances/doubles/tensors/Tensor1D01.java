package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.covectors.CoVec1D;

/**
 * Class to represent the space of (0,1)-tensors over the space of doubles. This is equivalent to the
 * linear maps from Double -> Double and therefore is isomorphic to the space of doubles themselves.
 */
public class Tensor1D01 extends TensorD<One, Zero, One> {

    public static final TensorD<One, Zero, One> INSTANCE = new Tensor1D01();

    public static final Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = Prod.pair(
            HomTuple.tuple(),
            HomTuple.tuple(Exp.asExponential(a -> DoubleField.INSTANCE.unit())));

    private Tensor1D01() {
        super(CoVec1D.INSTANCE);
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor,
        final HomTuple<Zero, Exp<HomTuple<One, Double>, Double>> dualVectors,
        final HomTuple<One, HomTuple<One, Double>> vectors) {
        
        return tensor.destroy(
            invalid -> 
                maps -> 
                    maps.zip(vectors)
                        .mapAll(pair -> pair.first().apply(pair.second())).at(Ordinal.ZERO_1));
    }

    @Override
    public List<Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>>> basis() {
        return List.of(UNIT);
    }

    /**
     * T(1) = k * 1
     */
    @Override
    public List<Prod<Double, Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>>>> decompose(
            final Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor) {

        return List.of(Prod.pair(
            tensor.second().at(Ordinal.ZERO_1).apply(HomTuple.tuple(DoubleField.INSTANCE.unit())),
            UNIT));
    }
}
