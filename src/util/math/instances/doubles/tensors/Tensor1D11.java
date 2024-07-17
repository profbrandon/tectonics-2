package util.math.instances.doubles.tensors;


import util.Functional;
import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Pred;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec1D;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent the space of (1,1)-tensors over the space of doubles.
 */
public class Tensor1D11 
    extends 
        TensorD<One, One, One> {

    public static final TensorD<One, One, One> INSTANCE = new Tensor1D11();

    /**
     * The unit (1,1)-tensor over the 1-dimensional vector space of doubles, i.e., the identity linear transformation.
     */
    public static final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = Prod.pair(
            HomTuple.tuple(Vec1D.INSTANCE.basis().get(0)),
            HomTuple.tuple(CoVec1D.INSTANCE.basis().get(0)));

    private Tensor1D11() {
        super(Ordinal.ONE_SET, Ordinal.ONE_SET, CoVec1D.INSTANCE);
    }

    @Override
    public Sum<Double, Exp<Prod<HomTuple<Pred<One>, Exp<HomTuple<One, Double>, Double>>, HomTuple<Pred<One>, HomTuple<One, Double>>>, Double>> 
        contract(
            final Ordinal<One> index, 
            final Ordinal<One> coindex,
            final Exp<Prod<HomTuple<One, Exp<HomTuple<One, Double>, Double>>, HomTuple<One, HomTuple<One, Double>>>, Double> tensor) {
        
        Preconditions.throwIfNull(index, "index");
        Preconditions.throwIfNull(coindex, "coindex");
        Preconditions.throwIfNull(tensor, "tensor");

        return Sum.left(
            Functional.let(toTensorProduct(tensor), tProd 
                -> tProd.second().at(coindex).apply(tProd.first().at(index))));
    }
}
