package util.math.vectorspaces;

import java.util.Collection;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.Field;

public abstract class NVectorSpace<N extends Cardinal, V, K> implements VectorSpace<HomTuple<N, V>, K> {

    private final VectorSpace<V, K> UNDERLYING;
    
    public NVectorSpace(final VectorSpace<V, K> underlyingSpace) {
        Preconditions.throwIfNull(underlyingSpace, "underlyingSpace");

        this.UNDERLYING = underlyingSpace;
    }

    public boolean equalsVector(final Collection<Ordinal<N>> enumerated, final HomTuple<N, V> v1, final HomTuple<N, V> v2) {
        return v1.equalsTuple(pair -> pair.destroy(a -> b -> UNDERLYING.equiv(a, b)), enumerated, v2);
    }

    public VectorSpace<V, K> underlyingVectorSpace() {
        return this.UNDERLYING;
    }

    @Override
    public Field<K> underlyingField() {
        return this.underlyingField();
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
