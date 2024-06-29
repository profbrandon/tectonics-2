package util.math;

public interface VectorSpace<V, K> {
    
    public V zero();

    public V sum(final V v1, final V v2);

    public V neg(final V v);

    public V scale(final V v, final K scalar);
}
