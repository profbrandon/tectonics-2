package util.math.vectorspaces;

import java.util.Collection;
import java.util.function.Function;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.counting.Prev;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.Field;

public abstract class PQTensorSpace<V, K, P extends Cardinal, Q extends Cardinal> 
    implements 
        TensorSpace<Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>>, V, K, P, Q> {

    private final Field<K> UNDERLYING_F;
    private final VectorSpace<V, K> UNDERLYING_V;
    private final FieldValuedSpace<V, K> UNDERLYING_DUAL;

    public PQTensorSpace(final FieldValuedSpace<V, K> underlyingDualSpace, final Field<K> underlyingField) {
        Preconditions.throwIfNull(underlyingDualSpace, "underlyingDualSpace");
        Preconditions.throwIfNull(underlyingField, "underlyingField");

        this.UNDERLYING_F = underlyingField;
        this.UNDERLYING_V = underlyingDualSpace.domainVectorSpace();
        this.UNDERLYING_DUAL = underlyingDualSpace;
    }

    public abstract Sum<K, Prod<HomTuple<Prev<P>, V>, HomTuple<Prev<Q>, Exp<V, K>>>> contract(
        final Ordinal<P> index1,
        final Ordinal<Q> index2,
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor);

    @Override
    public VectorSpace<V, K> underlyingVectorSpace() {
        return UNDERLYING_V;
    }

    @Override
    public FieldValuedSpace<V, K> underlyingDualSpace() {
        return UNDERLYING_DUAL;
    }

    @Override
    public Field<K> underlyingField() {
        return UNDERLYING_F;
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

    public K evaluate(
        final Collection<Ordinal<P>> pSet, 
        final Collection<Ordinal<Q>> qSet, 
        final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> tensor, 
        final HomTuple<P, Exp<V, K>> dualVectors, 
        final HomTuple<Q, V> vectors) {

        final Function<Prod<K, Prod<Exp<V, K>, V>>, K> reduce = 
        pair -> pair.destroy(
            k -> 
                p -> underlyingField().mult(k, p.first().apply(p.second())));

        return underlyingField().mult(
            dualVectors.zip(tensor.first())
                .eliminate(pSet, underlyingField().unit(), reduce),
            tensor.second().zip(vectors)
                .eliminate(qSet, underlyingField().unit(), reduce));
    }
}
