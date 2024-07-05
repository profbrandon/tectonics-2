package util.math.vectorspaces;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.counting.Prev;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Field;

public abstract class PQTensorSpace<V, K, P extends Cardinal, Q extends Cardinal> 
    implements 
        TensorSpace<Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>>, V, K, P, Q> {

    protected final Field<K> UNDERLYING_F;
    protected final VectorSpace<V, K> UNDERLYING_V;
    protected final VectorSpace<Exp<V, K>, K> UNDERLYING_DUAL;

    public PQTensorSpace(final VectorSpace<V, K> underlyingSpace, final Field<K> underlyingField) {
        Preconditions.throwIfNull(underlyingField, "underlyingField");
        this.UNDERLYING_F = underlyingField;
        this.UNDERLYING_V = underlyingSpace;
        this.UNDERLYING_DUAL = new FieldValuedSpace<>(underlyingSpace, underlyingField);
    }

    public Prod<HomTuple<Prev<P>, V>, HomTuple<Prev<Q>, Exp<V, K>>> contract(
        final Ordinal<P> index1,
        final Ordinal<Q> index2,
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor) {

        // TODO: Implement tensor contraction
        return null;
    }

    @Override
    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> scale(
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor, 
        final K scalar) {

        return Prod.map(
            tensor, 
            vectors -> 
                vectors.mapAll(
                    v -> UNDERLYING_V.scale(v, scalar)), 
            dualVectors -> 
                dualVectors.mapAll(
                    dv -> UNDERLYING_DUAL.scale(dv, scalar)));
    }

    @Override
    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> neg(final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor) {
        return this.scale(tensor, UNDERLYING_F.neg(UNDERLYING_F.unit()));
    }

    @Override
    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> zero() {
        return Prod.pair(
                new HomTuple<>(Ordinal.populate(UNDERLYING_V.zero())), 
                new HomTuple<>(Ordinal.populate(UNDERLYING_DUAL.zero())));
    }

    @Override
    public Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> sum(
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor1,
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor2) {
        
        return Prod.pair(
            tensor1.first().zip(tensor2.first()).mapAll(pair -> pair.destroy(a -> b -> UNDERLYING_V.sum(a, b))),
            tensor1.second().zip(tensor2.second()).mapAll(pair -> pair.destroy(a -> b -> UNDERLYING_DUAL.sum(a, b))));
    }
}
