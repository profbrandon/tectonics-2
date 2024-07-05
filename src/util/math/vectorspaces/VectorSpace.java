package util.math.vectorspaces;

import util.math.Field;
import util.math.Group;

public interface VectorSpace<V, K> extends Group<V> {
    
    public Field<K> underlyingField();

    public V scale(final V v, final K scalar);
}
