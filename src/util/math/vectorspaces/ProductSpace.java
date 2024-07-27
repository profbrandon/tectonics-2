package util.math.vectorspaces;

import util.Preconditions;
import util.data.algebraic.Prod;
import util.math.Field;

/**
 * Class to represent a mathematical cartesian product {@link VectorSpace} of two constituent
 * vector spaces.
 */
public class ProductSpace<V, W, K> 
    implements 
        VectorSpace<Prod<V, W>, K> {
    
    private final VectorSpace<V, K> LEFT_SPACE;
    private final VectorSpace<W, K> RIGHT_SPACE;

    /**
     * Builds the cartesian product space between the two vector spaces.
     * 
     * @param leftVectorSpace the left vector space
     * @param rightVectorSpace the right vector space
     */
    public ProductSpace(final VectorSpace<V, K> leftVectorSpace, final VectorSpace<W, K> rightVectorSpace) {
        Preconditions.throwIfDifferent(
            leftVectorSpace.underlyingField(), 
            rightVectorSpace.underlyingField(),
            "The two vector spaces disagree on the underlying field");

        this.LEFT_SPACE = leftVectorSpace;
        this.RIGHT_SPACE = rightVectorSpace;
    }

    /**
     * @return the left vector space
     */
    public VectorSpace<V, K> leftSpace() {
        return this.LEFT_SPACE;
    }

    /**
     * @return the right vector space
     */
    public VectorSpace<W, K> rightSpace() {
        return this.RIGHT_SPACE;
    }

    @Override
    public Field<K> underlyingField() {
        return LEFT_SPACE.underlyingField();
    }

    @Override
    public Prod<V, W> zero() {
        return Prod.pair(LEFT_SPACE.zero(), RIGHT_SPACE.zero());
    }

    @Override
    public Prod<V, W> sum(final Prod<V, W> m1, final Prod<V, W> m2) {
        return Prod.pair(
            LEFT_SPACE.sum(m1.first(), m2.first()),
            RIGHT_SPACE.sum(m1.second(), m2.second()));
    }

    @Override
    public Prod<V, W> neg(final Prod<V, W> g) {
        return Prod.pair(
            LEFT_SPACE.neg(g.first()), 
            RIGHT_SPACE.neg(g.second()));
    }

    @Override
    public Prod<V, W> scale(final Prod<V, W> v, final K scalar) {
        return Prod.pair(
            LEFT_SPACE.scale(v.first(), scalar),
            RIGHT_SPACE.scale(v.second(), scalar));
    }

    @Override
    public boolean equiv(final Prod<V, W> a1, final Prod<V, W> a2) {
        return 
            LEFT_SPACE.equiv(a1.first(), a2.first()) &&
            RIGHT_SPACE.equiv(a1.second(), a2.second());
    }
}
