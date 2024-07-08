package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.covectors.CoVec2D;

public class Tensor2D01 extends TensorD<Two, Zero, One> {

    public static final TensorD<Two, Zero, One> INSTANCE = new Tensor2D01();

    private Tensor2D01() {
        super(CoVec2D.INSTANCE);
    }

    @Override
    public List<Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>> basis() {
        return underlyingDualSpace()
            .basis()
            .stream()
            .map(b -> Prod.pair(
                HomTuple.<HomTuple<Two, Double>>tuple(),
                HomTuple.tuple(b)))
            .toList();
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>>> decompose(
        final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> v) {
        
        return List.of(Prod.pair(underlyingDualSpace().decompose(v.second().at(Ordinal.ZERO_1)).get(0).first(), basis().get(0)));
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor,
        final HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>> dualVectors,
        final HomTuple<One, HomTuple<Two, Double>> vectors) {
            
        return underlyingDualSpace().transform(tensor.second().at(Ordinal.ZERO_1), vectors.at(Ordinal.ZERO_1));
    }
    
}
