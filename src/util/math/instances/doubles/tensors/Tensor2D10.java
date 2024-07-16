package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.counting.Ordinal;
import util.counting.Pred;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;
import util.math.instances.doubles.vectors.Vec2D;

/**
 * Class to represent the space of (1,0)-tensors over the space of 2-dimensional double-valued vectors.
 */
public class Tensor2D10 extends TensorD<Two, One, Zero> {

    public static final TensorD<Two, One, Zero> INSTANCE = new Tensor2D10();

    /**
     * The unit "x" (1,0)-tensor over the 2-dimensional vector space of doubles.
     */
    public static final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>>
        UNIT_X = Prod.pair(
            HomTuple.tuple(Vec2D.UNIT_X),
            HomTuple.tuple());

    /**
     * The unit "y" (1,0)-tensor over the 2-dimensional vector space of doubles.
     */
    public static final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>>
        UNIT_Y = Prod.pair(
            HomTuple.tuple(Vec2D.UNIT_Y),
            HomTuple.tuple());

    private Tensor2D10() {
        super(CoVec2D.INSTANCE);
    }

    @Override
    public Sum<Double, Prod<HomTuple<Pred<One>, HomTuple<Two, Double>>, HomTuple<Pred<Zero>, Exp<HomTuple<Two, Double>, Double>>>>
        contract(
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
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>>>> 
        decompose(
            final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>> v) {

        return List.of(
            Prod.pair(
                underlyingVectorSpace().decompose(v.first().at(Ordinal.ZERO_1)).get(0).first(), 
                basis().get(0)));
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<Zero, Exp<HomTuple<Two, Double>, Double>>> tensor,
        final HomTuple<One, Exp<HomTuple<Two, Double>, Double>> dualVectors,
        final HomTuple<Zero, HomTuple<Two, Double>> vectors) {

        return evaluate(Ordinal.ONE_SET, Ordinal.ZERO_SET, tensor, dualVectors, vectors);
    }
}