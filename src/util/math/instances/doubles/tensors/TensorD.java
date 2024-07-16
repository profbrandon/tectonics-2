package util.math.instances.doubles.tensors;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.DualSpace;
import util.math.vectorspaces.TensorSpace;
import util.math.vectorspaces.finite.FiniteVectorSpace;

public abstract class TensorD<N extends Cardinal, P extends Cardinal, Q extends Cardinal>
    extends 
        TensorSpace<HomTuple<N, Double>, Double, P, Q>
    implements 
        FiniteVectorSpace<
            Exp<
                Prod<
                    HomTuple<P, Exp<HomTuple<N, Double>, Double>>, 
                    HomTuple<Q, HomTuple<N, Double>>
                    >,
                Double
            >, 
            Double
            > {

    public TensorD(
        final Collection<Ordinal<P>> pEnumerated, 
        final Collection<Ordinal<Q>> qEnumerated,
        final DualSpace<HomTuple<N, Double>, Double> dualSpace) {

        super(pEnumerated, qEnumerated, dualSpace);
    }
}
