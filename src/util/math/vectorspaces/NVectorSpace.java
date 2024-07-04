package util.math.vectorspaces;

import java.util.Collection;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.OrdinalSet;
import util.data.algebraic.HomTuple;

public abstract class NVectorSpace<O extends Ordinal, V, K> implements VectorSpace<HomTuple<O, V>, K> {

    private final VectorSpace<V, K> UNDERLYING;
    
    public NVectorSpace(final VectorSpace<V, K> underlyingSpace) {
        Preconditions.throwIfNull(underlyingSpace, "underlyingSpace");

        this.UNDERLYING = underlyingSpace;
    }

    public boolean equalsVector(final Collection<OrdinalSet<O>> enumerated, final HomTuple<O, V> v1, final HomTuple<O, V> v2) {
        return v1.equalsTuple(pair -> pair.destroy(a -> b -> UNDERLYING.equiv(a, b)), enumerated, v2);
    }

    public VectorSpace<V, K> underlyingVectorSpace() {
        return this.UNDERLYING;
    }

    @Override
    public HomTuple<O, V> zero() {
        return new HomTuple<>(OrdinalSet.populate(UNDERLYING.zero()));
    }

    @Override
    public HomTuple<O, V> sum(final HomTuple<O, V> v1, final HomTuple<O, V> v2) {
        return v1.mapEach(n -> vn -> UNDERLYING.sum(vn, v2.at(n)));
    }

    @Override
    public HomTuple<O, V> neg(final HomTuple<O, V> v) {
        return v.mapAll(a -> UNDERLYING.neg(a));
    }

    @Override
    public HomTuple<O, V> scale(final HomTuple<O, V> v, final K scalar) {
        return v.mapAll(a -> UNDERLYING.scale(a, scalar));
    }
}
