package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Prev;
import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;

public class Tensor2D01 extends TensorD<Two, Zero, One> {

    public static final TensorD<Two, Zero, One> INSTANCE = new Tensor2D01();

    private Tensor2D01() {
        super(CoVec2D.INSTANCE);
    }

    @Override
    public Sum<Double, Prod<HomTuple<Prev<Zero>, HomTuple<Two, Double>>, HomTuple<Prev<One>, Exp<HomTuple<Two, Double>, Double>>>> contract(
        final Ordinal<Zero> index1,
        final Ordinal<One> index2,
        final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor) {
        
        Preconditions.throwIfNull(index1, "index1");
        Preconditions.throwIfNull(index2, "index2");
        Preconditions.throwIfNull(tensor, "tensor");

        throw new IllegalStateException("Somehow an instance of Ordinal<Zero> was provided"); 
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
            
        return evaluate(Ordinal.ZERO_SET, Ordinal.ONE_SET, tensor, dualVectors, vectors);
    }
    
}
