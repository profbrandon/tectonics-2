package util.math.instances.doubles.tensors;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.covectors.CoVecD;
import util.math.vectorspaces.finite.FiniteDualSpace;
import util.math.vectorspaces.finite.FiniteMultilinearNFormSpace;
import util.math.vectorspaces.finite.FiniteTensorSpace;

public abstract class TensorD<N extends Cardinal, P extends Cardinal, Q extends Cardinal>
    extends 
        FiniteTensorSpace<HomTuple<N, Double>, Double, P, Q> {

    public TensorD(
        final Collection<Ordinal<P>> pEnumerated, 
        final Collection<Ordinal<Q>> qEnumerated,
        final CoVecD<N> dualSpace) {
        
        super(
            new FiniteMultilinearNFormSpace<P, Exp<HomTuple<N, Double>, Double>, Double>(pEnumerated, new FiniteDualSpace<>(dualSpace)), 
            new FiniteMultilinearNFormSpace<Q, HomTuple<N, Double>, Double>(qEnumerated, dualSpace));
    }
}
