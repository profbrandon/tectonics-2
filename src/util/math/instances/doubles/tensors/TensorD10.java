package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.OrdinalSet;
import util.counting.Ordinals.One;
import util.counting.Ordinals.Zero;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;

/**
 * Class to represent the space of (1,0)-tensors over the space of doubles. This is trivially isomorphic
 * to the space of doubles.
 */
public class TensorD10 extends TensorD<One, Zero> {
    
    public static final TensorD<One, Zero> INSTANCE = new TensorD10();

    public static final Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> UNIT = Prod.pair(
        new HomTuple<>(OrdinalSet.one(DoubleField.INSTANCE.unit())),
        new HomTuple<>(OrdinalSet.zero()));

    @Override
    public Double evaluate(
        final Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> tensor,
        final HomTuple<One, Exp<Double, Double>> dualVectors,
        final HomTuple<Zero, Double> vectors) {
            
        return tensor.destroy(values -> invalid -> dualVectors.zip(values).mapAll(pair -> pair.first().apply(pair.second())).at(OrdinalSet.ZERO_1));
    }

    @Override
    public List<Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>>>> decompose(
            Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> tensor) {

        return List.of(Prod.pair(tensor.first().at(OrdinalSet.ZERO_1), UNIT));
    }

    @Override
    public boolean equiv(
        final Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> v1,
        final Prod<HomTuple<One, Double>, HomTuple<Zero, Exp<Double, Double>>> v2) {
        
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        final List<Double> components1 = decompose(v1).stream().map(pair -> pair.first()).toList();
        final List<Double> components2 = decompose(v2).stream().map(pair -> pair.first()).toList();

        for (int i = 0; i < basis().size(); ++i) {
            if (!UNDERLYING_F.equiv(components1.get(i), components2.get(i))) return false;
        }

        return true;
    }
}
