package util.math.vectorspaces.finite;

import java.util.List;

import util.counting.Cardinal;
import util.counting.Combinatorics;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Identities;
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

    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        tensor(
            final HomTuple<P, V> vectors, 
            final HomTuple<Q, Exp<V, K>> covectors) {

        return fromTensorProduct(Prod.pair(vectors, covectors));
    }

    /**
     * Creates the multilinear map version of the given tensor product.
     * 
     * @param tProd the tensor product
     * @return the multilinear map version of the given tensor
     */
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        fromTensorProduct(
            final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tProd) {

        return Exp.asExponential(pair -> pair.<K>destroy(
            dv -> v ->
                underlyingField().mult(
                    underlyingField().multAll(
                        underlyingCovariantSpace().underlyingOrdinalSet()
                            .stream()
                            .map(ord -> dv.at(ord).apply(tProd.first().at(ord)))
                            .toList()),
                    underlyingField().multAll(
                        underlyingContravariantSpace().underlyingOrdinalSet()
                            .stream()
                            .map(ord -> tProd.second().at(ord).apply(v.at(ord)))
                            .toList()))));
    }

    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> 
        toTensorProduct(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> tensor) {

        final List<HomTuple<P, Exp<V, K>>> leftBasis = underlyingCovariantSpace().basis()
            .stream()
            .map(b -> underlyingCovariantSpace().dualAsVector(b))
            .toList();

        final List<HomTuple<Q, V>> rightBasis = underlyingContravariantSpace().basis()
            .stream()
            .map(b -> underlyingContravariantSpace().dualAsVector(b))
            .toList();

        final HomTuple<P, V> left = rightBasis
            .stream()
            .map(b -> 
                underlyingCovariantSpace()
                    .dualAsVector(
                        Exp.curry(Identities.expCommuteArgs(tensor))
                            .apply(b)))
            .map(v -> v.mapAll(f -> underlyingDualSpace().dualAsVector(f)))
            .reduce(
                HomTuple.all(underlyingDualSpace().domainVectorSpace().zero()),
                (tuple1, tuple2) -> new HomTuple<>(ord -> underlyingDualSpace().domainVectorSpace().sum(tuple1.at(ord), tuple2.at(ord))));

        final HomTuple<Q, Exp<V, K>> right = leftBasis
            .stream()
            .map(b ->
                underlyingContravariantSpace()
                    .dualAsVector(
                        Exp.curry(tensor)
                            .apply(b)))
            .map(v -> underlyingContravariantSpace().vectorAsDual(v))
            .map(v -> underlyingContravariantSpace().toLinear(v))
            .reduce(
                HomTuple.all(underlyingDualSpace().zero()),
                (tuple1, tuple2) -> new HomTuple<>(ord -> underlyingDualSpace().sum(tuple1.at(ord), tuple2.at(ord))));

        return Prod.pair(left, right);
    }

    @Override
    public List<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>> basis() {
        return 
            Combinatorics.cartesianProduct(
                    underlyingCovariantSpace().basis(), 
                    underlyingContravariantSpace().basis())
                .stream()
                .<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>>map(
                    funPair -> 
                        Exp.asExponential(
                            pair ->
                                pair.destroy(
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
        
        return null;
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
