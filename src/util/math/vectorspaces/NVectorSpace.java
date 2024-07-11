package util.math.vectorspaces;

import java.util.Collection;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.Field;

/**
 * Class to represent an {@code N}-dimensional {@link VectorSpace} of {@link HomTuple}s of some smaller
 * vector space over the same field. There are no other conditions besides the smaller space being a
 * vector space. All of the vector space operations are defined pointwise.
 */
public abstract class NVectorSpace<N extends Cardinal, V, K> implements VectorSpace<HomTuple<N, V>, K> {

    private final VectorSpace<V, K> UNDERLYING;
    
    /**
     * Builds an {@code N}-dimensional vector space with the information contained in the underlying
     * {@link VectorSpace}.
     * 
     * @param underlyingSpace the underlying vector space
     */
    public NVectorSpace(final VectorSpace<V, K> underlyingSpace) {
        Preconditions.throwIfNull(underlyingSpace, "underlyingSpace");
        this.UNDERLYING = underlyingSpace;
    }

    /**
     * Tests whether the given vectors are equivalent (up to the enumerated indices).
     * 
     * @param enumerated the enumerated indices to check for equality
     * @param v1 the first tuple vector
     * @param v2 the second tuple vector
     * @return whether the two tuple vectors are equivalent up to the provided indices
     */
    public boolean equalsVector(final Collection<Ordinal<N>> enumerated, final HomTuple<N, V> v1, final HomTuple<N, V> v2) {
        return v1.equalsTuple(pair -> pair.destroy(a -> b -> UNDERLYING.equiv(a, b)), enumerated, v2);
    }

    /**
     * @return the underlying {@link VectorSpace} that generates this tuple space
     */
    public VectorSpace<V, K> underlyingVectorSpace() {
        return this.UNDERLYING;
    }

    @Override
    public Field<K> underlyingField() {
        return UNDERLYING.underlyingField();
    }

    @Override
    public HomTuple<N, V> zero() {
        return new HomTuple<>(Ordinal.populate(UNDERLYING.zero()));
    }

    @Override
    public HomTuple<N, V> sum(final HomTuple<N, V> v1, final HomTuple<N, V> v2) {
        return v1.mapEach(n -> vn -> UNDERLYING.sum(vn, v2.at(n)));
    }

    @Override
    public HomTuple<N, V> neg(final HomTuple<N, V> v) {
        return v.mapAll(a -> UNDERLYING.neg(a));
    }

    @Override
    public HomTuple<N, V> scale(final HomTuple<N, V> v, final K scalar) {
        return v.mapAll(a -> UNDERLYING.scale(a, scalar));
    }
}
