package util.math.vectorspaces;

public interface InnerProductSpace<V, K> extends VectorSpace<V, K> {

    public K dot(final V v1, final V v2);
}