package util.math.vectorspaces;

import util.data.algebraic.Exp;
import util.math.Ring;

public class FieldValuedSpace<V, K> extends LinearMapSpace<V, K, K> implements Ring<Exp<V, K>> {

    /**
     * Creates a vector space of linear maps from a vector space to its underlying field.
     * 
     * @param underlyingSpace the vector space
     */
    public FieldValuedSpace(final VectorSpace<V, K> underlyingSpace) {
        super(underlyingSpace, underlyingSpace.underlyingField());
    }

    @Override
    public Exp<V, K> zero() {
        return Exp.constant(underlyingField().zero());
    }

    @Override
    public Exp<V, K> unit() {
        return Exp.constant(underlyingField().unit());
    }

    @Override
    public Exp<V, K> sum(final Exp<V, K> v1, final Exp<V, K> v2) {
        return Exp.asExponential(v -> underlyingField().sum(v1.apply(v), v2.apply(v)));
    }

    @Override
    public Exp<V, K> neg(final Exp<V, K> v) {
        return Exp.asExponential(w -> underlyingField().neg(v.apply(w)));
    }

    @Override
    public Exp<V, K> scale(final Exp<V, K> v, final K scalar) {
        return Exp.asExponential(w -> underlyingField().mult(v.apply(w), scalar));
    }

    @Override
    public Exp<V, K> mult(final Exp<V, K> r1, final Exp<V, K> r2) {
        return Exp.asExponential(v -> underlyingField().mult(r1.apply(v), r2.apply(v)));
    }

    /**
     * Returns true if they satisfy standard java equality since cannot easily be decided for functions.
     */
    @Override
    public boolean equiv(final Exp<V, K> a1, final Exp<V, K> a2) {
        return a1 == a2;
    }
}
