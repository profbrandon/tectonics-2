package util.math.vectorspaces;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.counting.Pred;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.Field;

/**
 * Interface to represent a mathematical (p,q)-tensor space of tensor values over the given {@link VectorSpace}.
 * Technically, this is the space of multilinear maps from p-tuples of covectors and q-tuples of vectors to the
 * underlying field.
 */
public abstract class TensorSpace<V, K, P extends Cardinal, Q extends Cardinal> 
    extends
        BilinearMapSpace<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>, K, K> {

    private final Collection<Ordinal<P>> P_ENUMERATED;
    private final Collection<Ordinal<Q>> Q_ENUMERATED;

    /**
     * Creates a (p,q)-tensor space given a {@link DualSpace} (which contains the underlying vector space).
     * 
     * @param pEnumerated the enumerated ordinal collection of cardinality p
     * @param qEnumerated the enumerated ordinal collection of cardinality q
     * @param dualSpace the dual space to use in constructing the tensor space
     */
    public TensorSpace(
        final Collection<Ordinal<P>> pEnumerated,
        final Collection<Ordinal<Q>> qEnumerated,
        final DualSpace<V, K> dualSpace) {
        super(
            new NVectorSpace<>(pEnumerated, dualSpace), 
            new NVectorSpace<>(qEnumerated, dualSpace.domainVectorSpace()),
            dualSpace.underlyingField());

        this.P_ENUMERATED = pEnumerated;
        this.Q_ENUMERATED = qEnumerated;
    }

    /**
     * Builds a tensor from the given vectors and covectors. {@code (v, dw)(du, t) = du(v) * dw(t)}.
     * 
     * @param vectors the vectors
     * @param covectors the covectors
     * @return a tensor whose action is defined by the given vectors and covectors.
     */
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        tensor(
            final HomTuple<P, V> vectors, 
            final HomTuple<Q, Exp<V, K>> covectors) {
    
        return Exp.asExponential(pair -> pair.destroy(
            ps -> 
                qs ->
                    underlyingField().mult(
                        ps.zip(vectors)
                            .mapAll(pair0 -> pair0.first().apply(pair0.second()))
                            .eliminate(
                                P_ENUMERATED, 
                                underlyingField().unit(), 
                                __ -> pair0 -> underlyingField().mult(pair0.first(), pair0.second())),
                        qs.zip(covectors)
                            .mapAll(pair0 -> pair0.second().apply(pair0.first()))
                            .eliminate(
                                Q_ENUMERATED,
                                underlyingField().unit(),
                                __ -> pair0 -> underlyingField().mult(pair0.first(), pair0.second())))));
    }

    /**
     * Constructs the tensor contraction of a (p,q)-tensor resulting in one of type (p-1,q-1). Note that
     * Contraction of (1, 1) tensor is required to be a member of the underlying field (i.e., a (0,0)-tensor
     * is a scalar).
     * 
     * @param index the index to eliminate
     * @param coindex the coindex to eliminate
     * @param tensor the tensor to contract
     * @return the contracted tensor (or a scalar)
     */
    public abstract Sum<K, Exp<Prod<HomTuple<Pred<P>, Exp<V, K>>, HomTuple<Pred<Q>, V>>, K>> 
        contract(
            final Ordinal<P> index,
            final Ordinal<Q> coindex,
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> tensor);

    @Override
    public Field<K> underlyingTargetSpace() {
        return underlyingField();
    }
}
