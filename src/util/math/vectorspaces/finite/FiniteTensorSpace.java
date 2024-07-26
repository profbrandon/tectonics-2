package util.math.vectorspaces.finite;

import java.util.List;

import util.counting.Cardinal;
import util.counting.Combinatorics;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.TensorSpace;

/**
 * Class to represent a {@link TensorSpace} that is also a {@link FiniteVectorSpace}, meaning
 * it derives its basis from the finite basis of the underlying {@link FiniteVectorSpace}.
 */
public abstract class FiniteTensorSpace<V, K, P extends Cardinal, Q extends Cardinal>
    extends 
        TensorSpace<V, K, P, Q>
    implements
        FiniteVectorSpace<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>, K> {

    private final FiniteMultilinearNFormSpace<P, Exp<V, K>, K> COVARIANT_PRODUCT_SPACE;
    private final FiniteMultilinearNFormSpace<Q, V, K> CONTRA_PRODUCT_SPACE;

    /**
     * Constructs a finite tensor space from the given dimensional information and the
     * underlying dual space.
     * 
     * @param pEnumerated the collection of (exhaustive) ordinals for the covariant dimension
     * @param qEnumerated the collection of (exhaustive) ordinals for the contravariant dimension
     * @param dualSpace the underlying dual space
     */
    public FiniteTensorSpace(
        final FiniteMultilinearNFormSpace<P, Exp<V, K>, K> covariantSpace,
        final FiniteMultilinearNFormSpace<Q, V, K> contravariantSpace) {

        super(
            covariantSpace, 
            contravariantSpace, 
            contravariantSpace.underlyingDualSpace());

        this.COVARIANT_PRODUCT_SPACE = covariantSpace;
        this.CONTRA_PRODUCT_SPACE    = contravariantSpace;
    }

    /**
     * Builds a tensor out of the given vectors and covectors.
     * 
     * @param vectors the covariant component
     * @param covectors the contravariant component
     * @return a (p,q)-tensor
     */
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        tensor(
            final HomTuple<P, V> vectors, 
            final HomTuple<Q, Exp<V, K>> covectors) {

        return fromTensorProduct(Prod.pair(vectors, covectors));
    }

    @Override
    public List<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>> basis() {
        return 
            Combinatorics.cartesianProduct(
                    underlyingCovariantSpace().basis(), 
                    underlyingContravariantSpace().basis())
                .stream()
                .<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>>map(
                    funPair -> Exp.asExponential(pair -> pair.destroy(
                        dv -> v -> 
                            underlyingField().mult(
                                funPair.first().apply(dv), 
                                funPair.second().apply(v)))))
                .toList();
    }

    @Override
    public List<Prod<K, Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>>> 
        decompose(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> tensor) {
        
        return 
            Combinatorics.cartesianProduct(
                    underlyingCovariantSpace().basisDualVectors(), 
                    underlyingContravariantSpace().basisDualVectors())
                .stream()
                .<Prod<K, Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>>>map(
                    basisVectors -> basisVectors.destroy(
                        leftBasis -> rightBasis ->
                            Prod.pair(
                                tensor.apply(Prod.pair(
                                    leftBasis
                                        .mapAll(underlyingDualSpace()::dualDualAsVector)
                                        .mapAll(underlyingDualSpace()::vectorAsDual),
                                    rightBasis
                                        .mapAll(underlyingDualSpace()::dualAsVector))), 
                                Exp.asExponential(pair -> pair.destroy(
                                    dv -> v -> 
                                        underlyingField().mult(
                                            underlyingCovariantSpace().fromLinearMaps(leftBasis).apply(dv), 
                                            underlyingContravariantSpace().fromLinearMaps(rightBasis).apply(v)))))))
                .toList();
    }

    @Override
    public FiniteDualSpace<V, K> underlyingDualSpace() {
        return underlyingContravariantSpace().underlyingDualSpace();
    }
    
    @Override
    public FiniteMultilinearNFormSpace<P, Exp<V, K>, K> underlyingCovariantSpace() {
        return COVARIANT_PRODUCT_SPACE;
    }

    @Override
    public FiniteMultilinearNFormSpace<Q, V, K> underlyingContravariantSpace() {
        return CONTRA_PRODUCT_SPACE;
    }
}
