package util.math.vectorspaces;

import util.data.algebraic.Exp;
import util.data.algebraic.Prod;

/**
 * Class to represent a bilinear map space, i.e., the space of linear maps from the product
 * space of two vector spaces to a third. In other words, this requires linearity in both
 * of the arguments.
 */
public abstract class BilinearMapSpace<V, W, U, K>
    extends
        LinearMapSpace<Prod<V, W>, U, K> {
    
    public final ProductSpace<V, W, K> DOMAIN_SPACE;

    /**
     * Constructs a bilinear map space.
     * 
     * @param leftVectorSpace the left domain space
     * @param rightVectorSpace the right domain space
     * @param targetVectorSpace the target vector space
     */
    public BilinearMapSpace(
        final VectorSpace<V, K> leftVectorSpace,
        final VectorSpace<W, K> rightVectorSpace,
        final VectorSpace<U, K> targetVectorSpace) {

        super(new ProductSpace<>(leftVectorSpace, rightVectorSpace), targetVectorSpace);
        this.DOMAIN_SPACE = new ProductSpace<>(leftVectorSpace, rightVectorSpace);
    }

    /**
     * Evaluation of the bilinear function on both {@link V} and {@link W} inputs.
     * 
     * @param bilinear the bilinear function
     * @param v the {@link V} vector input
     * @param w the {@link W} vector input
     * @return a vector in the target space
     */
    public U evaluate(final Exp<Prod<V, W>, U> bilinear, final V v, final W w) {
        return transform(bilinear, Prod.pair(v, w));
    }

    @Override
    public ProductSpace<V, W, K> domainVectorSpace() {
        return this.DOMAIN_SPACE;
    }
}
