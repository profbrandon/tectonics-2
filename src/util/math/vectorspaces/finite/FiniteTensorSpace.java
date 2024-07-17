package util.math.vectorspaces.finite;

import java.util.Collection;
import java.util.List;

import util.Functional;
import util.counting.Cardinal;
import util.counting.Combinatorics;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.ProductSpace;
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

    private final FiniteNSpace<P, V, K> COVARIANT_PRODUCT_SPACE;
    private final FiniteNDualSpace<Q, V, K> CONTRA_PRODUCT_SPACE;

    private final FiniteNDualSpace<P, V, K> P_DUAL_SPACE;
    private final FiniteNSpace<Q, V, K> Q_VECTOR_SPACE;

    /**
     * Constructs a finite tensor space from the given dimensional information and the
     * underlying dual space.
     * 
     * @param pEnumerated the collection of (exhaustive) ordinals for the covariant dimension
     * @param qEnumerated the collection of (exhaustive) ordinals for the contravariant dimension
     * @param dualSpace the underlying dual space
     */
    public FiniteTensorSpace(
        final Collection<Ordinal<P>> pEnumerated, 
        final Collection<Ordinal<Q>> qEnumerated,
        final FiniteDualSpace<V, K> dualSpace) {

        super(pEnumerated, qEnumerated, dualSpace);

        this.P_DUAL_SPACE   = new FiniteNDualSpace<>(pEnumerated, dualSpace);
        this.Q_VECTOR_SPACE = new FiniteNSpace<>(qEnumerated, dualSpace.domainVectorSpace());

        this.COVARIANT_PRODUCT_SPACE = new FiniteNSpace<>(pEnumerated, dualSpace.domainVectorSpace());
        this.CONTRA_PRODUCT_SPACE    = new FiniteNDualSpace<>(qEnumerated, dualSpace);
    }

    /**
     * Creates the tensor product version of the given multilinear map tensor.
     * 
     * @param tensor the multilinear map to convert to a tensor product
     * @return the tensor product version of the given tensor
     */
    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> 
        toTensorProduct(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> tensor) {

        return new ProductSpace<>(COVARIANT_PRODUCT_SPACE, CONTRA_PRODUCT_SPACE).sumAll(
            // We need the cartesian product of all combinations of the two sets of bases to feed to the tensor
            Combinatorics.cartesianProduct(
                    Combinatorics.nProduct(
                        List.copyOf(underlyingLeftSpace().underlyingOrdinalSet()), 
                        underlyingLeftSpace().underlyingVectorSpace().basis()),
                    Combinatorics.nProduct(
                        List.copyOf(underlyingRightSpace().underlyingOrdinalSet()), 
                        underlyingRightSpace().underlyingVectorSpace().basis()))
                .stream()
                .map(pair -> pair.destroy(
                    dv -> 
                    v -> Functional.let(
                        tensor.apply(Prod.pair(dv, v)), 
                        k -> Prod.pair(
                            COVARIANT_PRODUCT_SPACE.scale(dv.mapAll(underlyingLeftSpace().underlyingVectorSpace()::dualAsVector), k),
                            CONTRA_PRODUCT_SPACE.scale(v.mapAll(underlyingLeftSpace().underlyingVectorSpace()::vectorAsDual), k)))))
                .toList());
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

        return Exp.asExponential(pair -> pair.destroy(
            dv ->
                v -> underlyingField().mult(
                    underlyingLeftSpace().buildTensorFromProduct(dv).apply(tProd.first()),
                    CONTRA_PRODUCT_SPACE.buildTensorFromProduct(tProd.second()).apply(v))));
    }

    @Override
    public List<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>> basis() {
        return 
            Combinatorics.cartesianProduct(
                Combinatorics.nProduct(
                    List.copyOf(COVARIANT_PRODUCT_SPACE.underlyingOrdinalSet()), 
                    COVARIANT_PRODUCT_SPACE.underlyingVectorSpace().basis()), 
                Combinatorics.nProduct(
                    List.copyOf(CONTRA_PRODUCT_SPACE.underlyingOrdinalSet()),
                    CONTRA_PRODUCT_SPACE.underlyingVectorSpace().basis()))
            .stream()
            .map(this::fromTensorProduct)
            .toList();
    }

    @Override
    public List<Prod<K, Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>>> 
        decompose(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> tensor) {
        
        return basis()
            .stream()
            .map(b -> Functional.let(toTensorProduct(b), bProd ->
                Prod.pair(
                    tensor.apply(Prod.map(bProd, 
                        tuple -> tuple.mapAll(v -> underlyingLeftSpace().underlyingVectorSpace().vectorAsDual(v)),
                        tuple -> tuple.mapAll(dv -> underlyingLeftSpace().underlyingVectorSpace().dualAsVector(dv)))), 
                    b)))
            .toList();
    }

    @Override
    public FiniteNDualSpace<P, V, K> underlyingLeftSpace() {
        return P_DUAL_SPACE;
    }

    @Override
    public FiniteNSpace<Q, V, K> underlyingRightSpace() {
        return Q_VECTOR_SPACE;
    }
}
