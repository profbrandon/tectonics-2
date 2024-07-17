package util.math.vectorspaces.finite;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;

public class FiniteNDualSpace<N extends Cardinal, V, K>
    extends
        FiniteNSpace<N, Exp<V, K>, K> {

    private final FiniteDualSpace<V, K> FINITE_DUAL;

    public FiniteNDualSpace(final Collection<Ordinal<N>> enumerated, final FiniteDualSpace<V, K> underlyingVectorSpace) {
        super(enumerated, underlyingVectorSpace);
        this.FINITE_DUAL = underlyingVectorSpace;
    }

    @Override
    public FiniteDualSpace<V, K> underlyingVectorSpace() {
        return FINITE_DUAL;
    }

    public Exp<HomTuple<N, V>, K> tensorProduct(final HomTuple<N, Exp<V, K>> covectors) {
        return Exp.asExponential(
            tuple -> covectors.eliminate(
                underlyingOrdinalSet(), 
                underlyingField().unit(), 
                ord -> kcovector -> kcovector.destroy(
                    k -> 
                        covector -> underlyingField().mult(k, covector.apply(tuple.at(ord))))));
    }

    public HomTuple<N, Exp<V, K>> decomposeTensorProduct(final Exp<HomTuple<N, V>, K> tensor) {
        return new HomTuple<>(
            ord -> Exp.asExponential(
                v -> tensor.apply(
                    HomTuple.only(
                        ord, 
                        v, 
                        underlyingVectorSpace().domainVectorSpace().zero()))));
    }
}
