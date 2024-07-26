package util.math.vectorspaces;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;

/**
 * Class to represent a space of multilinear {@code N}-forms over some vector space. In other words,
 * it is the set of all multilinear functions from {@code N} copies of the underlying domain vector
 * space to the underlying field.
 */
public abstract class MultilinearNFormSpace<N extends Cardinal, V, K> 
    extends MultilinearNMapSpace<N, V, K, K> {

    private final DualSpace<V, K> DUAL_SPACE;

    /**
     * Creates a multilinear {@code N}-form space.
     * 
     * @param enumerated the enumerated indices (should be exhaustive)
     * @param underlyingDual the underlying dual space
     */
    public MultilinearNFormSpace(
        final Collection<Ordinal<N>> enumerated, 
        final DualSpace<V, K> underlyingDual) {

        super(enumerated, underlyingDual.domainVectorSpace(), underlyingDual.underlyingField());
    
        this.DUAL_SPACE = underlyingDual;
    }

    /**
     * @return the underlying dual vector space
     */
    public DualSpace<V, K> underlyingDualSpace() {
        return DUAL_SPACE;
    }

    /**
     * Takes an {@code N}-tuple of linear maps and creates a multilinear {@code N}-form.
     * This is allowable because of the following: <p>
     * 
     * Given {@code l, r : V -> K} linear, and {@code f(v, w) := l(v)r(w)},
     * <ul>
     *   <li>{@code f(av, w) = l(av)r(w) = al(v)r(w) = af(v,w)}</li>
     *   <li>{@code f(v + u, w) = l(v + u)r(w) = (l(v) + l(u))r(w) = l(v)r(w) + l(u)r(w) = f(v, w) + f(u, w)}</li>
     * </ul>
     * and so f is bilinear. This generalizes to this method which creates analagous multilinear forms.
     * 
     * @param linearMaps the linear maps to aggregate into a single multilinear map
     * @return a multilinear form on the underlying vector space
     */
    public Exp<HomTuple<N, V>, K> fromLinearMaps(final HomTuple<N, Exp<V, K>> linearMaps) {
        return Exp.asExponential(
            tuple -> 
                linearMaps.eliminate(
                    underlyingOrdinalSet(),
                    underlyingField().unit(),
                    ord -> pair -> pair.destroy(
                        k -> linear ->
                            underlyingField().mult(
                                k, 
                                linear.apply(tuple.at(ord))))));
    }

    /**
     * Takes an {@code N}-tuple of vectors and creates a multilinear map over the dual space.
     * 
     * @param vectors the vectors to transform into a multilinear map
     * @return a multilinear form on the underlying dual space
     */
    public Exp<HomTuple<N, Exp<V, K>>, K> fromVectors(final HomTuple<N, V> vectors) {
        return Exp.asExponential(
            tuple ->
                vectors.eliminate(
                    underlyingOrdinalSet(), 
                    underlyingField().unit(), 
                    ord -> pair -> pair.destroy(
                        k -> v -> 
                            underlyingField().mult(
                                k, 
                                tuple.at(ord).apply(v)))));
    }

    /**
     * Transforms {@code nV** -> K} into {@code nV -> K}.
     */
    public Exp<HomTuple<N, V>, K> dualDualAsVector(final Exp<HomTuple<N, Exp<Exp<V, K>, K>>, K> dualDual) {
        return Exp.asExponential(tuple -> dualDual.apply(tuple.mapAll(underlyingDualSpace()::vectorAsDualDual)));
    }
}
