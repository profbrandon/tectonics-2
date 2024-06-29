package util.math;

public interface VectorSpace<V, K> extends Group<V> {
    
    public V scale(final V v, final K scalar);
}
