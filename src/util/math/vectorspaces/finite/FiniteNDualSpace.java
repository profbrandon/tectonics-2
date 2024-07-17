package util.math.vectorspaces.finite;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;

/**
 * Class to represent a finite-dimensional tuple space over a given dual space. This is mostly
 * a convenience class to accommodate distributing exponentials through tuples.
 */
public class FiniteNDualSpace<N extends Cardinal, V, K>
    extends
        FiniteNSpace<N, Exp<V, K>, K> {

    private final FiniteDualSpace<V, K> FINITE_DUAL;

    /**
     * Builds a finite-dimensional tuple space over a dual vector space.
     * 
     * @param enumerated the (exhaustive) enumeration of the tuple dimensions
     * @param underlyingDualSpace the underlying dual space
     */
    public FiniteNDualSpace(final Collection<Ordinal<N>> enumerated, final FiniteDualSpace<V, K> underlyingDualSpace) {
        super(enumerated, underlyingDualSpace);
        this.FINITE_DUAL = underlyingDualSpace;
    }

    /**
     * Takes a tuple of covectors and transforms it into what is roughly a (0,n)-tensor.
     * 
     * @param covectors the product of covectors
     * @return the resulting tensor
     */
    public Exp<HomTuple<N, V>, K> buildTensorFromProduct(final HomTuple<N, Exp<V, K>> covectors) {
        return Exp.asExponential(
            tuple -> covectors.eliminate(
                underlyingOrdinalSet(), 
                underlyingField().unit(), 
                ord -> kcovector -> kcovector.destroy(
                    k -> 
                        covector -> underlyingField().mult(k, covector.apply(tuple.at(ord))))));
    }

    /**
     * Takes what is roughly a (0,n)-tensor and constructs an equivalent tuple of covectors.
     * 
     * @param tensor the tensor to convert
     * @return the equivalent tuple of covectors
     */
    public HomTuple<N, Exp<V, K>> buildProductFromTensor(final Exp<HomTuple<N, V>, K> tensor) {
        return new HomTuple<>(
            ord -> Exp.asExponential(
                v -> tensor.apply(
                    HomTuple.only(
                        ord, 
                        v, 
                        underlyingVectorSpace().domainVectorSpace().zero()))));
    }

    @Override
    public FiniteDualSpace<V, K> underlyingVectorSpace() {
        return FINITE_DUAL;
    }
}
