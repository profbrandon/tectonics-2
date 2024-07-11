package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.counting.Ordinal;
import util.counting.Prev;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;

public class Tensor2D10 extends TensorD<Two, One, Zero> {

    public static final TensorD<Two, One, Zero> INSTANCE = new Tensor2D10();

    private Tensor2D10() {
        super(CoVec2D.INSTANCE);
    }

    @Override
    public Sum<Double, Prod<HomTuple<Prev<One>, HomTuple<Two, Double>>, HomTuple<Prev<Zero>, Exp<HomTuple<Two, Double>, Double>>>> contract(
        final Ordinal<One> index1, 
        final Ordinal<Zero> index2,
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>> tensor) {
        
        Preconditions.throwIfNull(index1, "index1");
        Preconditions.throwIfNull(index2, "index2");
        Preconditions.throwIfNull(tensor, "tensor");
        
        throw new IllegalStateException("Somehow an instance of Ordinal<Zero> was provided"); 
    }

    @Override
    public List<Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>>> basis() {
        return underlyingVectorSpace()
            .basis()
            .stream()
            .map(b -> Prod.pair(
                HomTuple.tuple(b), 
                HomTuple.<Exp<HomTuple<Two, Double>, Double>>tuple()))
            .toList();
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>>>> decompose(
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>> v) {

        return List.of(Prod.pair(underlyingVectorSpace().decompose(v.first().at(Ordinal.ZERO_1)).get(0).first(), basis().get(0)));
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>> tensor,
        final HomTuple<One, Exp<HomTuple<Two, Double>, Double>> dualVectors,
        final HomTuple<Zero, HomTuple<Two, Double>> vectors) {

        return evaluate(Ordinal.ONE_SET, Ordinal.ZERO_SET, tensor, dualVectors, vectors);
    }
    
}