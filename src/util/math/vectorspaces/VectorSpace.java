package util.math.vectorspaces;

import util.math.Field;
import util.math.Group;

/**
 * Class to represent a mathematical vector space over the given vector data type and field type.
 * Every vector space has an underlying {@link Field}. To be a vector space, the vector space
 * must be a {@link Group} under vector addition that is compatible with scaling vectors by an
 * element of the field as well as distributing scalars over vectors and vice versa:
 * 
 * <ul>
 *   <li>Scalar Compatability - {@code a(bv) = (a * b)v}</li>
 *   <li>Scalar Distributivity - {@code a(v + w) = av + aw}</li>
 *   <li>Vector Distributivity - {@code (a + b)v = av + bv}</li>
 * </ul>
 */
public interface VectorSpace<V, K> extends Group<V> {
    
    /**
     * @return the underlying {@link Field} of the vector space
     */
    public Field<K> underlyingField();

    /**
     * Scales this vector by the given scalar. Formalizes the notion of "stretching" in a
     * particular direction.
     * 
     * @param v the vector to scale
     * @param scalar the value to scale the vector by
     * @return the stretched vector
     */
    public V scale(final V v, final K scalar);
}
