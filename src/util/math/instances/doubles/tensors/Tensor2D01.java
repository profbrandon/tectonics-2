package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Pred;
import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;

/**
 * Class to represent the space of (0,1)-tensors over the space of 2-dimensional double-valued vectors.
 */
public class Tensor2D01 
    extends 
        TensorD<Two, Zero, One> {

    public static final TensorD<Two, Zero, One> INSTANCE = new Tensor2D01();

    /**
     * The unit "x" (0,1)-tensor over the 2-dimensional vector space of doubles.
     */
    public static final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>
        UNIT_X = Prod.pair(
            HomTuple.tuple(),
            HomTuple.tuple(CoVec2D.UNIT_X));

    /**
     * The unit "y" (0,1)-tensor over the 2-dimensional vector space of doubles.
     */
    public static final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>
        UNIT_Y = Prod.pair(
            HomTuple.tuple(),
            HomTuple.tuple(CoVec2D.UNIT_Y));

    private Tensor2D01() {
        super(CoVec2D.INSTANCE);
    }

    @Override
    public Sum<Double, Prod<HomTuple<Pred<Zero>, HomTuple<Two, Double>>, HomTuple<Pred<One>, Exp<HomTuple<Two, Double>, Double>>>>
        contract(
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
    public List<Prod<Double, Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>>> 
        decompose(
            final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> v) {
        
        return List.of(
            Prod.pair(
                underlyingDualSpace().decompose(v.second().at(Ordinal.ZERO_1)).get(0).first(),
                basis().get(0)));
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<Zero, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor,
        final HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>> dualVectors,
        final HomTuple<One, HomTuple<Two, Double>> vectors) {
            
        return evaluate(Ordinal.ZERO_SET, Ordinal.ONE_SET, tensor, dualVectors, vectors);
    }
}
