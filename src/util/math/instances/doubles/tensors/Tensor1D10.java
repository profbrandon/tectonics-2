package util.math.instances.doubles.tensors;

import util.counting.Ordinal;
import util.counting.Pred;
import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec1D;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent the space of (1,0)-tensors over the space of doubles. This is trivially isomorphic
 * to the space of doubles.
 */
public class Tensor1D10 
    extends 
        TensorD<One, One, Zero> {

    public static final TensorD<One, One, Zero> INSTANCE = new Tensor1D10();

    /**
     * The unit (1,0)-tensor over the 1-dimensional vector space of doubles, i.e., the {@link Double} value 1.0.
     */
    public static final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = Prod.pair(
            HomTuple.tuple(Vec1D.INSTANCE.basis().get(0)),
            HomTuple.tuple());

    private Tensor1D10() {
        super(Ordinal.ONE_SET, Ordinal.ZERO_SET, CoVec1D.INSTANCE);
    }

    @Override
    public Sum<Double, Exp<Prod<HomTuple<Pred<One>, Exp<HomTuple<One, Double>, Double>>, HomTuple<Pred<Zero>, HomTuple<One, Double>>>, Double>> 
        contract(
            final Ordinal<One> index, 
            final Ordinal<Zero> coindex,
            final Exp<Prod<HomTuple<One, Exp<HomTuple<One, Double>, Double>>, HomTuple<Zero, HomTuple<One, Double>>>, Double> tensor) {
        
        Preconditions.throwIfNull(index, "index");
        Preconditions.throwIfNull(coindex, "coindex");
        Preconditions.throwIfNull(tensor, "tensor");

        throw new IllegalStateException("Somehow an instance of Ordinal<Zero> was provided"); 
    }
}
