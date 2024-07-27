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
public class NVectorSpace<N extends Cardinal, V, K> 
    implements 
        VectorSpace<HomTuple<N, V>, K> {

    protected final Collection<Ordinal<N>> ENUMERATED;
    private final VectorSpace<V, K> UNDERLYING;
    
    /**
     * Builds an {@code N}-dimensional vector space with the information contained in the underlying
     * {@link VectorSpace}.
     * 
     * @param enumerated the set of ordinals of the corresponding cardinality
     * @param underlyingSpace the underlying vector space
     */
    public NVectorSpace(final Collection<Ordinal<N>> enumerated, final VectorSpace<V, K> underlyingSpace) {

        // TODO: I would like to check that enumerated is exhaustive

        Preconditions.throwIfContainsNull(enumerated, "enumerated");
        Preconditions.throwIfNull(underlyingSpace, "underlyingSpace");

        this.ENUMERATED = enumerated;
        this.UNDERLYING = underlyingSpace;
    }

    /**
     * @return the underlying {@link VectorSpace} that generates this tuple space
     */
    public VectorSpace<V, K> underlyingVectorSpace() {
        return this.UNDERLYING;
    }

    /**
     * @return the underlying collection of {@link Ordinal}s used to enumerate the tuples
     */
    public Collection<Ordinal<N>> underlyingOrdinalSet() {
        return this.ENUMERATED;
    }

    @Override
    public Field<K> underlyingField() {
        return UNDERLYING.underlyingField();
    }

    @Override
    public HomTuple<N, V> zero() {
        return HomTuple.all(underlyingVectorSpace().zero());
    }

    @Override
    public HomTuple<N, V> sum(final HomTuple<N, V> v1, final HomTuple<N, V> v2) {
        Preconditions.throwIfNull(v1, "v1");
        return new HomTuple<>(
            ord -> underlyingVectorSpace()
                .sum(v1.at(ord), v2.at(ord)));
    }

    @Override
    public HomTuple<N, V> neg(final HomTuple<N, V> v) {
        Preconditions.throwIfNull(v, "v");
        return v.mapAll(a -> UNDERLYING.neg(a));
    }

    @Override
    public HomTuple<N, V> scale(final HomTuple<N, V> v, final K scalar) {
        Preconditions.throwIfNull(v, "v");
        return v.mapAll(a -> UNDERLYING.scale(a, scalar));
    }

    @Override
    public boolean equiv(final HomTuple<N, V> a1, final HomTuple<N, V> a2) {
        return a1.equalsTuple(
            pair -> pair.destroy(x1 -> x2 -> underlyingVectorSpace().equiv(x1, x2)), 
            ENUMERATED, 
            a2);
    }
}
