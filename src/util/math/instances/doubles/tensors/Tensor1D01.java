package util.math.instances.doubles.tensors;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Pred;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec1D;

/**
 * Class to represent the space of (0,1)-tensors over the space of doubles. This is equivalent to the
 * linear maps from Double -> Double and therefore is isomorphic to the space of doubles themselves.
 */
public class Tensor1D01 
    extends 
        TensorD<One, Zero, One> {

    public static final TensorD<One, Zero, One> INSTANCE = new Tensor1D01();

    /**
     * The unit (0,1)-tensor over the 1-dimensional vector space of doubles, i.e., the identity linear map.
     */
    public static final Prod<HomTuple<Zero, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = null;
    
    private Tensor1D01() {
        super(Ordinal.ZERO_SET, Ordinal.ONE_SET, CoVec1D.INSTANCE);
    }

    @Override
    public Sum<Double, Exp<Prod<HomTuple<Pred<Zero>, Exp<HomTuple<One, Double>, Double>>, HomTuple<Pred<One>, HomTuple<One, Double>>>, Double>>
        contract(
            final Ordinal<Zero> index1,
            final Ordinal<One> index2,
            final Exp<Prod<HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>, HomTuple<One, HomTuple<One, Double>>>, Double> tensor) {
        
        Preconditions.throwIfNull(index1, "index1");
        Preconditions.throwIfNull(index2, "index2");
        Preconditions.throwIfNull(tensor, "tensor");

        throw new IllegalStateException("Somehow an instance of Ordinal<Zero> was provided"); 
    }
}
