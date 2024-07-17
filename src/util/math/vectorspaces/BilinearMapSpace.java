package util.math.vectorspaces;

import util.Preconditions;
import util.data.algebraic.Exp;
import util.data.algebraic.Prod;
import util.math.Field;

/**
 * Class to represent a bilinear map space, i.e., the space of functions from the cartesian
 * product of two vector spaces which acts as a linear map upon partial application in either
 * argument:
 * 
 * <ul>
 *   <li>{@code f(s * v, w) = s * f(v, w)}</li>
 *   <li>{@code f(v, s * w) = s * f(v, w)}</li>
 *   <li>{@code f(v, w + y) = f(v, w) + f(v, y)}</li>
 *   <li>{@code f(v + x, w) = f(v, w) + f(x, w)}</li>
 * </ul>
 */
public abstract class BilinearMapSpace<V, W, U, K>
    implements
        VectorSpace<Exp<Prod<V, W>, U>, K> {

    private final VectorSpace<V, K> LEFT_SPACE;
    private final VectorSpace<W, K> RIGHT_SPACE;
    private final VectorSpace<U, K> TARGET_SPACE;

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

        Preconditions.throwIfNull(leftVectorSpace, "leftVectorSpace");
        Preconditions.throwIfNull(rightVectorSpace, "rightVectorSpace");
        Preconditions.throwIfNull(targetVectorSpace, "targetVectorSpace");
        Preconditions.throwIfDifferent(
            leftVectorSpace.underlyingField(), 
            rightVectorSpace.underlyingField(), 
            "The domain spaces do not agree on the underlying field.");
        Preconditions.throwIfDifferent(
            leftVectorSpace.underlyingField(),
            targetVectorSpace.underlyingField(),
            "The domain and codomain spaces do not agree on the underlying field.");

        this.LEFT_SPACE   = leftVectorSpace;
        this.RIGHT_SPACE  = rightVectorSpace;
        this.TARGET_SPACE = targetVectorSpace;
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
        return bilinear.apply(Prod.pair(v, w));
    }

    /**
     * Converts the given bilinear map to a linear map from the left space to the target.
     * The way this is done is throught partial application of the bilinear map.
     * 
     * @param bilinear the bilinear map
     * @param w the element of the right space to hold fixed
     * @return a linear map
     */
    public Exp<V, U> leftLinearMap(final Exp<Prod<V, W>, U> bilinear, final W w) {
        return Exp.asExponential(v -> evaluate(bilinear, v, w));
    }

    /**
     * Converts the given bilinear map to a linear map from the right space to the target.
     * The way this is done is throught partial application of the bilinear map.
     * 
     * @param bilinear the bilinear map
     * @param v the element of the left space to hold fixed
     * @return a linear map
     */
    public Exp<W, U> rightLinearMap(final Exp<Prod<V, W>, U> bilinear, final V v) {
        return Exp.asExponential(w -> evaluate(bilinear, v, w));
    }

    /**
     * @return the underlying left domain vector space
     */
    public VectorSpace<V, K> underlyingLeftSpace() {
        return LEFT_SPACE;
    }

    /**
     * @return the underlying right domain vector space
     */
    public VectorSpace<W, K> underlyingRightSpace() {
        return RIGHT_SPACE;
    }

    /**
     * @return the underlying target vector space
     */
    public VectorSpace<U, K> underlyingTargetSpace() {
        return TARGET_SPACE;
    }

    @Override
    public Field<K> underlyingField() {
        return TARGET_SPACE.underlyingField();
    }

    @Override
    public Exp<Prod<V, W>, U> neg(final Exp<Prod<V, W>, U> bilinear) {
        return Exp.asExponential(pair -> evaluate(bilinear, underlyingLeftSpace().neg(pair.first()), pair.second()));
    }

    @Override
    public Exp<Prod<V, W>, U> zero() {
        return Exp.asExponential(pair -> underlyingTargetSpace().zero());
    }

    @Override
    public Exp<Prod<V, W>, U> sum(final Exp<Prod<V, W>, U> m1, final Exp<Prod<V, W>, U> m2) {
        return Exp.asExponential(pair -> underlyingTargetSpace().sum(m1.apply(pair), m2.apply(pair)));
    }

    @Override
    public Exp<Prod<V, W>, U> scale(final Exp<Prod<V, W>, U> v, final K scalar) {
        return Exp.asExponential(pair -> evaluate(v, underlyingLeftSpace().scale(pair.first(), scalar), pair.second()));
    }
}
