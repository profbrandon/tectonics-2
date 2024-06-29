package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.counting.Ordinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

public class Tensor01D extends TensorD<Zero, One> {

    public static final TensorD<Zero, One> INSTANCE = new Tensor01D();

    @Override
    public Double evaluate(
        final Prod<HomTuple<Zero, Double>, HomTuple<One, Exp<Double, Double>>> tensor,
        final HomTuple<Zero, Exp<Double, Double>> dualVectors,
        final HomTuple<One, Double> vectors) {
        
        return tensor.destroy(invalid -> maps -> maps.zip(vectors).mapAll(pair -> pair.first().apply(pair.second())).at(OrdinalSet.ZERO_1));
    }
}
