package util.math.vectorspaces.finite;

import java.util.List;

import util.data.algebraic.Exp;
import util.data.algebraic.Prod;
import util.math.Field;
import util.math.vectorspaces.DualSpace;

/**
 * Class to represent a finite dual vector space to some given {@link FiniteVectorSpace} instance.
 * Note that all of the necessary information in the dual space is contained within the parent
 * vector space.
 */
public class FiniteDualSpace<V, K> 
    extends
        DualSpace<V, K> 
    implements
        FiniteVectorSpace<Exp<V, K>, K> {

    private final FiniteVectorSpace<V, K> UNDERLYING_SPACE;

    /**
     * Constructor to build a finite dual vector space from the given finite vector space.
     * 
     * @param underlyingSpace the underlying finite space
     */
    public FiniteDualSpace(final FiniteVectorSpace<V, K> underlyingSpace) {
        super(underlyingSpace);
        this.UNDERLYING_SPACE = underlyingSpace;
    }

    /**
     * A messy isomorphism that maps dual vectors to vectors by mapping the components
     * over the dual basis vectors to the components over the basis vectors, i.e., this
     * satisfies:
     * 
     * {@code db.apply(dualAsVector(dv)) == dv.apply(b)} where db(b) = 1.
     * 
     * @param dualvector the dual vector to translate into a vector
     * @return a vector representation of the dual vector
     */
    public V dualAsVector(final Exp<V, K> dualvector) {
        return domainVectorSpace()
            .sumAll(
                domainVectorSpace()
                    .basis()
                    .stream()
                    .map(b -> domainVectorSpace().scale(b, dualvector.apply(b)))
                    .toList());
    }

    /**
     * A messy isomorphism that maps vectors to dual vectors by mapping the components
     * over the dual basis vectors to the components over the basis vectors.
     * 
     * @param v the vector to translate into a dual vector
     * @return a dual vector representation of the vector
     */
    public Exp<V, K> vectorAsDual(final V v) {
        return Exp.asExponential(
            w -> util.data.algebraic.List.list(
                domainVectorSpace().decompose(v)).zip(
                    util.data.algebraic.List.list(domainVectorSpace().decompose(w)))
                        .map(pair -> pair.destroy(
                            left ->
                                right -> underlyingField().mult(left.first(), right.first())))
                        .foldr(underlyingField().zero(), a -> b -> underlyingField().sum(a, b)));
    }

    /**
     * The natural (in the category theoretic sense) isomorphism between a finite vector space and its
     * double-dual. {@code ddv == ddv(db)b}
     * 
     * @param ddvector the double-dual vector
     * @return the vector corresponding to the given double-dual vector
     */
    public V dualDualAsVector(final Exp<Exp<V, K>, K> ddvector) {
        return this.domainVectorSpace()
            .sumAll(
                basis()
                    .stream()
                    .map(b -> this.domainVectorSpace().scale(dualAsVector(b), ddvector.apply(b)))
                    .toList());
    }

    @Override
    public FiniteVectorSpace<V, K> domainVectorSpace() {
        return this.UNDERLYING_SPACE;
    }

    @Override
    public Field<K> targetVectorSpace() {
        return this.UNDERLYING_SPACE.underlyingField();
    }
    
    @Override
    public List<Exp<V, K>> basis() {
        return UNDERLYING_SPACE.basis()
            .stream()
            .map(this::vectorAsDual)
            .toList();
    }

    @Override
    public List<Prod<K, Exp<V, K>>> decompose(Exp<V, K> v) {
        return domainVectorSpace()
            .basis()
            .stream()
            .map(b -> Prod.pair(transform(v, b), vectorAsDual(b)))
            .toList();
    }
}
