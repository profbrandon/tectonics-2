package util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;

public class Tensor11D extends TensorD<One, One> {

    public static final TensorD<One, One> INSTANCE = new Tensor11D();

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
    
}
