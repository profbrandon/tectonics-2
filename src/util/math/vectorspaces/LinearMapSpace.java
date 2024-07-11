package util.math.vectorspaces;

import util.Preconditions;
import util.data.algebraic.Exp;
import util.math.Field;

/**
 * Class to represent a linear map space between two {@link VectorSpace}s over a common {@link Field}.
 * As the name implies, this class should only be used to operate on linear maps, i.e., functions of
 * the form {@code l : V -> W} that satisfy the linearity conditions:
 * 
 * <ul>
 *   <li>{@code l(av) = a * l(v)}</li>
 *   <li>{@code l(v + w) = l(v) + l(w)}</li>
 * </ul>
 */
public abstract class LinearMapSpace<V, W, K> implements VectorSpace<Exp<V, W>, K> {

    private final VectorSpace<V, K> DOMAIN;
    private final VectorSpace<W, K> TARGET;

    /**
     * Constructs a linear map space from the domain vector space to the target vector space.
     * 
     * @param domainSpace the domain {@link VectorSpace}
     * @param targetSpace the target (codomain) {@link VectorSpace}
     */
    public LinearMapSpace(final VectorSpace<V, K> domainSpace, final VectorSpace<W, K> targetSpace) {
        Preconditions.throwIfNull(domainSpace, "domainSpace");
        Preconditions.throwIfNull(targetSpace, "targetSpace");
        Preconditions.throwIfDifferent(
            domainSpace.underlyingField(), 
            targetSpace.underlyingField(),
            "Underlying fields do not agree.");

        this.DOMAIN = domainSpace;
        this.TARGET = targetSpace;
    }

    /**
     * @return the underlying domain {@link VectorSpace}
     */
    public VectorSpace<V, K> domainVectorSpace() {
        return this.DOMAIN;
    }

    /**
     * @return the underlying target {@link VectorSpace}
     */
    public VectorSpace<W, K> targetVectorSpace() {
        return this.TARGET;
    }

    /**
     * Transforms a vector by supplying it to the provided linear map.
     * 
     * @param linear the linear map to apply
     * @param vector the vector to transform
     * @return the resulting vector after the linear transformation
     */
    public W transform(final Exp<V, W> linear, final V vector) {
        Preconditions.throwIfNull(linear, "linear");
        Preconditions.throwIfNull(vector, "vector");
        return linear.apply(vector);
    }

    @Override
    public Field<K> underlyingField() {
        return targetVectorSpace().underlyingField();
    }

    @Override
    public Exp<V, W> zero() {
        return Exp.constant(TARGET.zero());
    }

    @Override
    public Exp<V, W> sum(final Exp<V, W> l1, final Exp<V, W> l2) {
        return Exp.asExponential(v -> TARGET.sum(l1.apply(v), l2.apply(v)));
    }

    @Override
    public Exp<V, W> neg(final Exp<V, W> linear) {
        return Exp.asExponential(v -> TARGET.neg(linear.apply(v)));
    }

    @Override
    public Exp<V, W> scale(final Exp<V, W> linear, final K scalar) {
        return Exp.asExponential(v -> TARGET.scale(linear.apply(v), scalar));
    }
}
