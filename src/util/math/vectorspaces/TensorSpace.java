package util.math.vectorspaces;

import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;

public interface TensorSpace<T, V, K, P extends Cardinal, Q extends Cardinal> extends VectorSpace<T, K> {

    public K evaluate(final T tensor, final HomTuple<P, Exp<V, K>> dualVectors, final HomTuple<Q, V> vectors);
}
