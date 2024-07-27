package util.math.instances.doubles.vectors;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;
import util.math.vectorspaces.InnerProductSpace;
import util.math.vectorspaces.finite.FiniteNSpace;

/**
 * Class to represent an {@code N}-dimensional vector space of doubles.
 */
public class VecD<N extends Cardinal> 
    extends 
        FiniteNSpace<N, Double, Double> 
    implements 
        InnerProductSpace<HomTuple<N, Double>, Double> {

    protected VecD(final Collection<Ordinal<N>> enumerated) {
        super(enumerated, DoubleField.INSTANCE);
    }

    @Override
    public Double dot(final HomTuple<N, Double> v1, final HomTuple<N, Double> v2) {
        final HomTuple<N, Double> prod = v1
            .zip(v2)
                .mapAll(pair -> 
                    pair.destroy(a -> b -> DoubleField.INSTANCE.mult(a, b)));

        return DoubleField.INSTANCE.sumAll(underlyingOrdinalSet().stream().map(prod::at).toList());
    }
}
