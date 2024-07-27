package util.math.vectorspaces;

import util.Preconditions;
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
    implements
        VectorSpace<Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K>, K> {

    private final MultilinearNFormSpace<P, Exp<V, K>, K> COVARIANT_SPACE;
    private final MultilinearNFormSpace<Q, V, K>         CONTRAVARIANT_SPACE;

    private final DualSpace<V, K> DUAL_SPACE;

    /**
     * Creates a (p,q)-tensor space given a {@link DualSpace} (which contains the underlying vector space).
     * 
     * @param pEnumerated the enumerated ordinal collection of cardinality p
     * @param qEnumerated the enumerated ordinal collection of cardinality q
     * @param dualSpace the dual space to use in constructing the tensor space
     */
    public TensorSpace(
        final MultilinearNFormSpace<P, Exp<V, K>, K> covariantSpace, 
        final MultilinearNFormSpace<Q, V, K> contravariantSpace,
        final DualSpace<V, K> underlyingDual) {

        Preconditions.throwIfNull(covariantSpace, "covariantSpace");
        Preconditions.throwIfNull(contravariantSpace, "contravariantSpace");
        Preconditions.throwIfDifferent(
            covariantSpace.underlyingField(), 
            contravariantSpace.underlyingField(), 
            "The covariant and contravariant spaces must agree on the underlying field.");

        this.COVARIANT_SPACE     = covariantSpace;
        this.CONTRAVARIANT_SPACE = contravariantSpace;

        this.DUAL_SPACE = underlyingDual;
    }

    /**
     * @return the underlying covariant space of (p,0)-tensors
     */
    public MultilinearNFormSpace<P, Exp<V, K>, K> underlyingCovariantSpace() {
        return COVARIANT_SPACE;
    }

    /**
     * @return the underlying contravariant space of (0,q)-tensors
     */
    public MultilinearNFormSpace<Q, V, K> underlyingContravariantSpace() {
        return CONTRAVARIANT_SPACE;
    }

    /**
     * @return the underlying dual vector space
     */
    public DualSpace<V, K> underlyingDualSpace() {
        return DUAL_SPACE;
    }

    /**
     * Creates a multilinear map from the two tuples of linear maps from {@code V* -> K} and {@code V -> K}.
     * 
     * @param left the tuple of covariant linear maps {@code V* -> K}
     * @param right the tuple of contravariant linear maps {@code V -> K}
     * @return a (p,q)-tensor with the given covariant and contravariant components
     */
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        fromLinearMaps(
            final HomTuple<P, Exp<Exp<V, K>, K>> left, 
            final HomTuple<Q, Exp<V, K>> right) {

        return Exp.asExponential(pair -> pair.destroy(
            leftTuple ->
                rightTuple ->
                    underlyingField().mult(
                        underlyingCovariantSpace().fromLinearMaps(left).apply(leftTuple),
                        underlyingContravariantSpace().fromLinearMaps(right).apply(rightTuple))));
    }

    /**
     * Creates a tensor from the given "tensor product" of vectors and covectors.
     * 
     * @param product the tensor product
     * @return the corresponding tensor
     */
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        fromTensorProduct(
            final Prod<HomTuple<P, V>, HomTuple<Q, Exp<V, K>>> product) {

        return fromLinearMaps(
            product.first().mapAll(underlyingDualSpace()::vectorAsDualDual),
            product.second());
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
    public Field<K> underlyingField() {
        return COVARIANT_SPACE.underlyingField();
    }

    @Override
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        neg(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> g) {
                
        return Exp.asExponential(pair -> underlyingField().neg(g.apply(pair)));
    }

    @Override
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> zero() {
        return Exp.constant(underlyingField().zero());
    }

    @Override
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        sum(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> m1,
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> m2) {
        
        return Exp.asExponential(pair -> underlyingField().sum(m1.apply(pair), m2.apply(pair)));
    }

    @Override
    public Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> 
        scale(
            final Exp<Prod<HomTuple<P, Exp<V, K>>, HomTuple<Q, V>>, K> v, K scalar) {
        
        return Exp.asExponential(pair -> underlyingField().mult(v.apply(pair), scalar));
    }
}
