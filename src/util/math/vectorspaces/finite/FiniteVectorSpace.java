package util.math.vectorspaces.finite;

import java.util.List;

import util.data.algebraic.Prod;
import util.math.vectorspaces.VectorSpace;

/**
 * Interface to represent a finite-dimensional {@link VectorSpace}. The set of
 * basis vectors provided by {@link FiniteVectorSpace#basis()} should satisfy
 * two conditions: linear independence and spanning.
 * 
 * <ul>
 *   <li>Linear Independence - {@code sum(i = 0; i < dimension(); scale(basis().get(i), as.get(i))) == 0 if and only if as.get(j) == 0 for all j}</li>
 *   <li>Spanning - {@code any vector v satisfies v = sum(i = 0; i < dimension(); scale(basis.get(i), vs.get(i))) for some list of values vs}
 * </ul>
 * 
 * In effect, this requires {@code decompose(zero()).stream().map(Prod::first) == [0, ...]} and
 * {@code v = sumAll(decompose(v).stream().map(pair -> destroy(vi -> b -> scale(b, vi))).toList())}
 */
public interface FiniteVectorSpace<V, K> 
    extends 
        VectorSpace<V, K> {
    
    /**
     * @return the list of basis vectors
     */
    public List<V> basis();

    /**
     * Builds the vector decomposition, i.e., takes a vector and translates it into scalar-basisvector pairs
     * that can be used to distinguish the vector.
     * 
     * @param v the vector to decompose
     * @return a list of component-basisvector pairs that are equivalent to the given vector
     */
    public List<Prod<K, V>> decompose(final V v);

    /**
     * @return the dimension of this vector space
     */
    public default int dimension() {
        return basis().size();
    }

    @Override
    default boolean equiv(final V a1, final V a2) {
        final List<Prod<K, V>> components1 = decompose(a1);
        final List<Prod<K, V>> components2 = decompose(a2);

        if (components1.size() != components2.size()) throw new IllegalStateException("The number of vector components do not match");

        for (int i = 0; i < dimension(); ++i) {
            if (!underlyingField().equiv(components1.get(i).first(), components2.get(i).first())) return false;
        }

        return true;
    }
}
