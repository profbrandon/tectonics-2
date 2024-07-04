package util.math.vectorspaces;

import util.Preconditions;
import util.data.algebraic.Exp;

public abstract class LinearMapSpace<V, W, K> implements VectorSpace<Exp<V, W>, K> {

    private final VectorSpace<V, K> DOMAIN;
    private final VectorSpace<W, K> TARGET;

    public LinearMapSpace(final VectorSpace<V, K> domainSpace, final VectorSpace<W, K> targetSpace) {
        Preconditions.throwIfNull(domainSpace, "domainSpace");
        Preconditions.throwIfNull(targetSpace, "targetSpace");

        this.DOMAIN = domainSpace;
        this.TARGET = targetSpace;
    }

    public VectorSpace<V, K> domainVectorSpace() {
        return this.DOMAIN;
    }

    public VectorSpace<W, K> targetVectorSpace() {
        return this.TARGET;
    }

    public W transform(final Exp<V, W> linear, final V vector) {
        return linear.apply(vector);
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
