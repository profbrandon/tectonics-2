package util.math.vectorspaces;

import util.data.algebraic.Exp;
import util.data.algebraic.Prod;

public abstract class BilinearFormSpace<V, W, K>
    extends 
        BilinearMapSpace<V, W, K, K> {

    public BilinearFormSpace(
        final VectorSpace<V, K> leftVectorSpace, 
        final VectorSpace<W, K> rightVectorSpace,
        final VectorSpace<K, K> targetVectorSpace) {

        super(leftVectorSpace, rightVectorSpace, targetVectorSpace);
    }

    public Exp<Prod<V, W>, K> fromLinear(final Exp<V, K> left, final Exp<W, K> right) {
        return Exp.asExponential(pair -> pair.destroy(
            v -> w -> underlyingField().mult(
                left.apply(v), 
                right.apply(w))));
    }
}
