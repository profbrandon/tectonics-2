package util.math.vectorspaces.finite;

import java.util.Collection;
import java.util.List;

import util.counting.Cardinal;
import util.counting.Combinatorics;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.MultilinearNFormSpace;

/**
 * The finite-dimensional counterpart to {@link MultilinearNFormSpace}.
 */
public class FiniteMultilinearNFormSpace<N extends Cardinal, V, K>
    extends
        MultilinearNFormSpace<N, V, K>
    implements
        FiniteVectorSpace<Exp<HomTuple<N, V>, K>, K> {

    private final FiniteDualSpace<V, K> FINITE_DUAL;

    /**
     * Constructs a finite-dimensional multilinear {@code N}-form space.
     * 
     * @param enumerated the enumerated indices (should be exhaustive)
     * @param finiteDualSpace the underlying finite dual space.
     */
    public FiniteMultilinearNFormSpace(
        final Collection<Ordinal<N>> enumerated,
        final FiniteDualSpace<V, K> finiteDualSpace) {

        super(enumerated, finiteDualSpace);

        this.FINITE_DUAL = finiteDualSpace;
    }

    /**
     * Transforms {@code nV -> K} into {@code nV** -> K}.
     */
    public Exp<HomTuple<N, Exp<Exp<V, K>, K>>, K> vectorAsDualDual(final Exp<HomTuple<N, V>, K> vector) {
        return Exp.asExponential(tuple -> vector.apply(tuple.mapAll(underlyingDualSpace()::dualDualAsVector)));
    }

    /**
     * Transforms {@code nV -> K} into {@code nV* -> K} using a messy isomorphism.
     */
    public Exp<HomTuple<N, Exp<V, K>>, K> vectorAsDual(final Exp<HomTuple<N, V>, K> vector) {
        return Exp.asExponential(tuple -> vector.apply(tuple.mapAll(underlyingDualSpace()::dualAsVector)));
    }

    /**
     * Transforms {@code nV* -> K} into {@code nV -> K} using a messy isomorphism.
     */
    public Exp<HomTuple<N, V>, K> dualAsVector(final Exp<HomTuple<N, Exp<V, K>>, K> covector) {
        return Exp.asExponential(tuple -> covector.apply(tuple.mapAll(underlyingDualSpace()::vectorAsDual)));
    }

    /**
     * @return the linear map version of the basis
     */
    public List<HomTuple<N, Exp<V, K>>> basisDualVectors() {
        return Combinatorics.nProduct(
            List.copyOf(underlyingOrdinalSet()), 
            underlyingDualSpace().basis());
    }

    @Override
    public FiniteVectorSpace<V, K> underlyingDomainSpace() {
        return this.FINITE_DUAL.domainVectorSpace();
    }

    @Override
    public FiniteDualSpace<V, K> underlyingDualSpace() {
        return FINITE_DUAL;
    }

    @Override
    public List<Exp<HomTuple<N, V>, K>> basis() {
        return basisDualVectors()
            .stream()
            .map(this::fromLinearMaps)
            .toList();
    }

    @Override
    public List<Prod<K, Exp<HomTuple<N, V>, K>>> decompose(Exp<HomTuple<N, V>, K> v) {
        return
            Combinatorics.nProduct(
                    List.copyOf(underlyingOrdinalSet()),
                    underlyingDualSpace().domainVectorSpace().basis())
                .stream()
                .map(
                    baseTuple -> 
                        Prod.pair(
                            v.apply(baseTuple), 
                            fromLinearMaps(baseTuple.mapAll(underlyingDualSpace()::vectorAsDual))))
                .toList();
    }
}
