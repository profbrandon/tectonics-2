package util.math.instances.doubles.tensors;

import util.counting.Ordinal;
import util.counting.Pred;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Identities;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;

import java.util.List;

import util.Functional;
import util.Preconditions;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Three;
import util.math.instances.doubles.covectors.CoVec3D;

/**
 * Class to represent the space of (1,1)-tensors over the space of 3-dimensional double-valued vectors.
 */
public class Tensor3D11
    extends
        TensorD<Three, One, One> {
    
    public static final TensorD<Three, One, One> INSTANCE = new Tensor3D11();

    public Tensor3D11() {
        super(Ordinal.ONE_SET, Ordinal.ONE_SET, CoVec3D.INSTANCE);
    }

    /**
     * Builds the linear map equivalent to the given (1,1)-tensor.
     * 
     * @param tensor the given (1,1)-tensor
     * @return the linear map equivalent to the given tensor
     */
    public static Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> 
        asLinearMap(
            final Exp<Prod<HomTuple<One, Exp<HomTuple<Three, Double>, Double>>, HomTuple<One, HomTuple<Three, Double>>>, Double> tensor) {

        return Exp.asExponential(
            v -> 
                INSTANCE.underlyingDualSpace().dualDualAsVector(
                    Exp.asExponential(
                        w -> 
                            Exp.curry(Identities.expCommuteArgs(tensor))
                                .apply(HomTuple.tuple(v))
                                .apply(HomTuple.tuple(w)))));
    }

    @Override
    public Sum<Double, Exp<Prod<HomTuple<Pred<One>, Exp<HomTuple<Three, Double>, Double>>, HomTuple<Pred<One>, HomTuple<Three, Double>>>, Double>> 
        contract(
            final Ordinal<One> index, 
            final Ordinal<One> coindex,
            final Exp<Prod<HomTuple<One, Exp<HomTuple<Three, Double>, Double>>, HomTuple<One, HomTuple<Three, Double>>>, Double> tensor) {
        
        Preconditions.throwIfNull(index, "index");
        Preconditions.throwIfNull(coindex, "coindex");
        Preconditions.throwIfNull(tensor, "tensor");

        return Sum.left(
            underlyingField().sumAll(
                Functional.let(decompose(tensor).stream().map(Prod::first).toList(), components 
                    -> List.of(components.get(0), components.get(4), components.get(8)))));
    }
}
