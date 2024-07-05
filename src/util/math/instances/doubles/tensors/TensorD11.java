package util.math.instances.doubles.tensors;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.vectors.Vec1D;

public class TensorD11 extends TensorD<One, One, One> {

    public static final TensorD<One, One, One> INSTANCE = new TensorD11();

    public static final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> 
        UNIT = Prod.pair(
            HomTuple.tuple(HomTuple.tuple(DoubleField.INSTANCE.unit())),
            HomTuple.tuple(Exp.constant(DoubleField.INSTANCE.unit())));

    private TensorD11() {
        super(Vec1D.INSTANCE);
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
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>>>> decompose(
            final Prod<HomTuple<One, HomTuple<One, Double>>, HomTuple<One, Exp<HomTuple<One, Double>, Double>>> tensor) {
        
        return List.of(Prod.pair(evaluate(tensor, UNIT.second(), UNIT.first()), UNIT));
    }
}
