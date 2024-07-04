package util.math.vectorspaces;

import util.math.Group;

public interface VectorSpace<V, K> extends Group<V> {
    
    public V scale(final V v, final K scalar);
}
