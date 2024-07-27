package util.math.vectorspaces;

import java.util.Collection;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.Field;

/**
 * Class to represent a space of multilinear {@code N}-forms over some vector space to another
 * target vector space. In other words, it is the set of all multilinear functions from {@code N} 
 * copies of the underlying domain vector space to the target space.
 */
public abstract class MultilinearNMapSpace<N extends Cardinal, V, U, K>
    implements 
        VectorSpace<Exp<HomTuple<N, V>, U>, K> {

    private final Collection<Ordinal<N>> ENUMERATED;
    private final VectorSpace<V, K> DOMAIN_SPACE;
    private final VectorSpace<U, K> TARGET_SPACE;

    /**
     * Creates a multilinear map space from {@code N} copies of the domain to the target.
     * 
     * @param enumerated the enumerated indices (should be exhaustive)
     * @param underlyingDomainSpace the underlying domain vector space
     * @param underlyingTargetSpace the underlying target vector space
     */
    public MultilinearNMapSpace(
        final Collection<Ordinal<N>> enumerated,
        final VectorSpace<V, K> underlyingDomainSpace,
        final VectorSpace<U, K> underlyingTargetSpace) {

        Preconditions.throwIfContainsNull(enumerated, "enumerated");
        Preconditions.throwIfNull(underlyingDomainSpace, "underlyingDomainSpace");
        Preconditions.throwIfNull(underlyingTargetSpace, "underlyingTargetSpace");
        Preconditions.throwIfDifferent(
            underlyingDomainSpace.underlyingField(), 
            underlyingTargetSpace.underlyingField(), 
            "The target and domain must agree on the underlying field.");

        this.ENUMERATED   = enumerated;
        this.DOMAIN_SPACE = underlyingDomainSpace;
        this.TARGET_SPACE = underlyingTargetSpace;
    }

    /**
     * @return the underlying domain space
     */
    public VectorSpace<V, K> underlyingDomainSpace() {
        return DOMAIN_SPACE;
    }

    /**
     * @return the underlying target space
     */
    public VectorSpace<U, K> underlyingTargetSpace() {
        return TARGET_SPACE;
    }

    /**
     * @return the underlying collection of ordinals
     */
    public Collection<Ordinal<N>> underlyingOrdinalSet() {
        return ENUMERATED;
    }

    @Override
    public Exp<HomTuple<N, V>, U> neg(final Exp<HomTuple<N, V>, U> g) {
        return Exp.asExponential(tuple -> underlyingTargetSpace().neg(g.apply(tuple)));
    }

    @Override
    public Exp<HomTuple<N, V>, U> zero() {
        return Exp.asExponential(tuple -> underlyingTargetSpace().zero());
    }

    @Override
    public Exp<HomTuple<N, V>, U> sum(final Exp<HomTuple<N, V>, U> m1, final Exp<HomTuple<N, V>, U> m2) {
        return Exp.asExponential(tuple -> underlyingTargetSpace().sum(m1.apply(tuple), m2.apply(tuple)));
    }

    @Override
    public Exp<HomTuple<N, V>, U> scale(final Exp<HomTuple<N, V>, U> v, K scalar) {
        return Exp.asExponential(tuple -> underlyingTargetSpace().scale(v.apply(tuple), scalar));
    }

    @Override
    public Field<K> underlyingField() {
        return DOMAIN_SPACE.underlyingField();
    }
}
