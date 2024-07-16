package util.math.vectorspaces.finite;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.TensorSpace;

/**
 * Class to represent a {@link TensorSpace} that is also a {@link FiniteVectorSpace}, meaining
 * it derives its basis from the finite basis of the underlying {@link FiniteVectorSpace}.
 */
public abstract class FiniteTensorSpace<V, K, P extends Cardinal, Q extends Cardinal>
    extends 
        TensorSpace<V, K, P, Q>
    implements
        FiniteVectorSpace<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>, K> {

    public FiniteTensorSpace(
        final Collection<Ordinal<P>> pEnumerated, 
        final Collection<Ordinal<Q>> qEnumerated,
        final FiniteDualSpace<V, K> dualSpace) {

        super(pEnumerated, qEnumerated, dualSpace);
    }
}
