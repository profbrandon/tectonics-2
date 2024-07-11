package util.math.vectorspaces;

import java.util.Collection;

import util.math.Field;
import util.math.Group;

public interface VectorSpace<V, K> extends Group<V> {
    
    public Field<K> underlyingField();

    public V scale(final V v, final K scalar);

    default V sumAll(final Collection<V> vs) {
        V temp = zero();

        for (final V v : vs) {
            temp = sum(temp, v);
        }

        return temp;
    }
}
