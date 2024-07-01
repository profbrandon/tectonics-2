package util.math;

import java.util.List;

import util.data.algebraic.Prod;

public interface FiniteVectorSpace<V, K> extends VectorSpace<V, K> {
    
    public List<V> basis();

    public List<Prod<K, V>> decompose(final V v);
}
