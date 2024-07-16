package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Pred;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.DoubleField;
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
        super(CoVec1D.INSTANCE);
    }

    @Override
    public Sum<Double, Prod<HomTuple<Pred<One>, HomTuple<One, Double>>, HomTuple<Pred<One>, Exp<HomTuple<One, Double>, Double>>>> 
        contract(
            final Ordinal<One> index1,
            final Ordinal<One> index2,
            final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor) {
        
        Preconditions.throwIfNull(index1, "index1");
        Preconditions.throwIfNull(index2, "index2");
        Preconditions.throwIfNull(tensor, "tensor");

        return Sum.left(tensor.destroy(v -> dv -> dv.at(index2).apply(v.at(index1))));
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor,
        final HomTuple<One, Exp<HomTuple<One, Double>, Double>> dualVectors,
        final HomTuple<One, HomTuple<One, Double>> vectors) {

        return tensor.destroy(values -> maps -> 
            DoubleField.INSTANCE.mult(
                dualVectors.zip(values)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(Ordinal.ZERO_1),
                maps.zip(vectors)
                    .mapAll(pair -> pair.first().apply(pair.second()))
                    .at(Ordinal.ZERO_1)));
    }

    @Override
    public List<Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>>>>
        decompose(
            final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor) {
        
        return List.of(Prod.pair(
            this.evaluate(tensor, UNIT.second(), UNIT.first()), 
            UNIT));
    }
}
