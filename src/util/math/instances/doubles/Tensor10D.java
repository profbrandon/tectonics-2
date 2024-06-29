package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.counting.Ordinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

public class Tensor10D extends TensorD<One, Zero> {
    
    public static final TensorD<Zero, One> INSTANCE = new Tensor01D();

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> tensor,
        final HomTuple<One, Exp<Double, Double>> dualVectors,
        final HomTuple<Zero, Double> vectors) {
            
        return tensor.destroy(values -> invalid -> dualVectors.zip(values).mapAll(pair -> pair.first().apply(pair.second())).at(OrdinalSet.ZERO_1));
    }
}
