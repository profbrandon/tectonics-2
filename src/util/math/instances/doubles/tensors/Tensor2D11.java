package util.math.instances.doubles.tensors;

import util.Functional;
import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Pred;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;

/**
 * Class to represent the space of (1,1)-tensors over the space of 2-dimensional double-valued vectors.
 */
public class Tensor2D11 
    extends 
        TensorD<Two, One, One> {
    
    public static final TensorD<Two, One, One> INSTANCE = new Tensor2D11();

    private Tensor2D11() {
        super(Ordinal.ONE_SET, Ordinal.ONE_SET, CoVec2D.INSTANCE);
    }

    /**
     * Builds the linear map equivalent to the given (1,1)-tensor.
     * 
     * @param tensor the given (1,1)-tensor
     * @return the linear map equivalent to the given tensor
     */
    public Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> 
        asLinearMap(
            final Exp<Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>, Double> tensor) {

        return Exp.asExponential(
            v -> 
                CoVec2D.INSTANCE.dualDualAsVector(Exp.asExponential(
                    w -> Exp.curry(tensor)
                        .apply(HomTuple.tuple(v))
                        .apply(HomTuple.tuple(w)))));
    }

    @Override
    public Sum<Double, Exp<Prod<HomTuple<Pred<One>, Exp<HomTuple<Two, Double>, Double>>, HomTuple<Pred<One>, HomTuple<Two, Double>>>, Double>> 
        contract(
            final Ordinal<One> index, 
            final Ordinal<One> coindex,
            final Exp<Prod<HomTuple<One, Exp<HomTuple<Two, Double>, Double>>, HomTuple<One, HomTuple<Two, Double>>>, Double> tensor) {
        
        Preconditions.throwIfNull(index, "index");
        Preconditions.throwIfNull(coindex, "coindex");
        Preconditions.throwIfNull(tensor, "tensor");

        return Sum.left(
            Functional.let(toTensorProduct(tensor), tProd 
                -> tProd.second().at(coindex).apply(tProd.first().at(index))));
    }
}
