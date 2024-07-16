package util.math.vectorspaces;

/**
 * Interface to represent a {@link VectorSpace} that is an inner product space. Formally, this
 * means there is some "dot" product between vectors that produces a scalar, satisfying
 * the following:
 * 
 * <ul>
 *   <li>Distributivity - {@code v * (u + w) = v * u + v * w}</li>
 *   <li>Positive-Definiteness - {@code v * v >= 0} for all {@code v}</li>
 * </ul>
 * 
 * Note that this implies the underlying field should be ordered.
 */
public interface InnerProductSpace<V, K> 
    extends 
        VectorSpace<V, K> {

    /**
     * The dot product between two vectors. This should distribute over vector addition and
     * be positive definite.
     * 
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product of the two vectors
     */
    public K dot(final V v1, final V v2);
}