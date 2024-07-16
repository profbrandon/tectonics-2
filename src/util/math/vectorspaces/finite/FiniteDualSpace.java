package util.math.vectorspaces.finite;

import java.util.List;

import util.data.algebraic.Exp;
import util.math.Field;
import util.math.vectorspaces.DualSpace;

/**
 * Class to represent a finite dual vector space to some given {@link FiniteVectorSpace} instance.
 * Note that all of the necessary information in the dual space is contained within the parent
 * vector space.
 */
public abstract class FiniteDualSpace<V, K> 
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
     * A messy isomorphism that maps dual vectors to covectors by mapping the components
     * over the dual basis vectors to the components over the basis vectors, i.e., this
     * satisfies:
     * 
     * {@code db.apply(dualAsVector(dv)) == dv.apply(b)} where db(b) = 1.
     * 
     * @param dualvector the dual vector to translate into a vector
     * @return a vector representation of the dual vector
     */
    public V dualAsVector(final Exp<V, K> dualvector) {
        return this.domainVectorSpace()
            .sumAll(
                this.domainVectorSpace()
                    .basis()
                    .stream()
                    .map(b -> this.domainVectorSpace().scale(b, dualvector.apply(b)))
                    .toList());
    }

    /**
     * The natural (in the category theoretic sense) isomorphism between a vector space and its
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
            .map(this::covectorFromBasisVector)
            .toList();
    }

    /**
     * Creates the given covector from the given basis vector.
     * 
     * @param b the basis vector to rebuild as a covector
     * @return the corresponding covector
     */
    protected Exp<V, K> covectorFromBasisVector(final V b) {
        return Exp.<V, K>asExponential(
            v -> 
                UNDERLYING_SPACE
                    .decompose(v)
                    .stream()
                    .map(pair -> pair.destroy(
                        vi -> 
                            bi -> 
                                bi.equals(b) ? this.targetVectorSpace().zero() : vi))
                    .reduce(this.targetVectorSpace()::sum)
                    .get());
    }
}
