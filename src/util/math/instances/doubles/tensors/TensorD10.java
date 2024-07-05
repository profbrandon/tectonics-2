package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent the space of (1,0)-tensors over the space of doubles. This is trivially isomorphic
 * to the space of doubles.
 */
public class TensorD10 extends TensorD<One, One, Zero> {

    public static final TensorD<One, One, Zero> INSTANCE = new TensorD10();

    public static final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = Prod.pair(
            HomTuple.tuple(Vec1D.INSTANCE.basis().get(0)),
            HomTuple.tuple());

    private TensorD10() {
        super(Vec1D.INSTANCE);
    }

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>> tensor,
        final HomTuple<One, Exp<HomTuple<One, Double>, Double>> dualVectors,
        final HomTuple<Zero, HomTuple<One, Double>> vectors) {
            
        return tensor.destroy(values -> invalid -> dualVectors.zip(values).mapAll(pair -> pair.first().apply(pair.second())).at(Ordinal.ZERO_1));
    }

    @Override
    public List<Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>>>> decompose(
        final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<Zero, Exp<HomTuple<One, Double>, Double>>> tensor) {

        return List.of(Prod.pair(Vec1D.extract(tensor.first().at(Ordinal.ZERO_1)), UNIT));
    }
}
