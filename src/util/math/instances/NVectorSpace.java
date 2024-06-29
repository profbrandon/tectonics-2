package util.math.instances;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.OrdinalSet;
import util.data.algebraic.NTuple;
import util.math.VectorSpace;

public class NVectorSpace<O extends Ordinal, V, K> implements VectorSpace<NTuple<O, V>, K> {

    private final VectorSpace<V, K> UNDERLYING;
    
    public NVectorSpace(final VectorSpace<V, K> underlyingSpace) {
        Preconditions.throwIfNull(underlyingSpace, "underlyingSpace");

        this.UNDERLYING = underlyingSpace;
    }

    @Override
    public NTuple<O, V> zero() {
        return new NTuple<>(OrdinalSet.populate(UNDERLYING.zero()));
    }

    @Override
    public NTuple<O, V> sum(NTuple<O, V> v1, NTuple<O, V> v2) {
        return null;
    }

    @Override
    public NTuple<O, V> neg(NTuple<O, V> v) {
        return null;
    }

    @Override
    public NTuple<O, V> scale(NTuple<O, V> v, K scalar) {
        return null;
    }
}
