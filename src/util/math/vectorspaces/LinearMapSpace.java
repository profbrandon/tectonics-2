package util.math.vectorspaces;

import util.Preconditions;
import util.data.algebraic.Exp;
import util.math.VectorSpace;

public abstract class LinearMapSpace<V, W, K> implements VectorSpace<Exp<V, W>, K> {

    private final VectorSpace<W, K> TARGET;

    public LinearMapSpace(final VectorSpace<W, K> targetSpace) {
        Preconditions.throwIfNull(targetSpace, "targetSpace");

        this.TARGET = targetSpace;
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
