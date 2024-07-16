package util.math.vectorspaces.finite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.NVectorSpace;

/**
 * Class to represent a finite-dimensional version of {@link NVectorSpace}. The basis for this
 * space is generated via the underlying vector space's basis. For example, if the underlying
 * space has dimension 3 with basis vectors {@code b1, b2, b3}, then the {@link Cardinals.Two}
 * finite n-space's basis is the following:<p/>
 * 
 * {@code [ (b1, 0), (b2, 0), (b3, 0), (0, b1), (0, b2), (0, b3) ]}
 */
public class FiniteNSpace<N extends Cardinal, V, K>
    extends
        NVectorSpace<N, V, K>
    implements
        FiniteVectorSpace<HomTuple<N, V>, K> {

    private final FiniteVectorSpace<V, K> FINITE_SPACE;

    /**
     * Creates a finite-dimensional vector space of {@code N}-tuples of some underlying
     * finite vector space.
     * 
     * @param enumerated the collection of ordinals to use (should have cardinality {@code N})
     * @param underlyingVectorSpace the underlying finite vector space
     */
    public FiniteNSpace(final Collection<Ordinal<N>> enumerated, final FiniteVectorSpace<V, K> underlyingVectorSpace) {
        super(enumerated, underlyingVectorSpace);
        this.FINITE_SPACE = underlyingVectorSpace;
    }

    @Override
    public FiniteVectorSpace<V, K> underlyingVectorSpace() {
        return FINITE_SPACE;
    }

    @Override
    public List<HomTuple<N, V>> basis() {
        return underlyingVectorSpace()
            .basis()
            .stream()
            .flatMap(
                b -> ENUMERATED
                    .stream()
                    .map(
                        ord1 -> 
                            new HomTuple<N, V>(
                                ord2 -> 
                                    ord1.equalsOrdinal(ord2) ? b : underlyingVectorSpace().zero())))
            .toList();
    }

    @Override
    public List<Prod<K, HomTuple<N, V>>> decompose(final HomTuple<N, V> nVector) {
        return nVector.eliminate(
            ENUMERATED, 
            new ArrayList<Prod<K, HomTuple<N, V>>>(), 
            ord1 -> 
                lv -> lv.destroy(
                    l -> 
                        v -> {
                            l.addAll(underlyingVectorSpace()
                                .decompose(v)
                                .stream()
                                .map(kb -> kb.destroy(
                                    k -> 
                                        b -> Prod.pair(
                                            k, 
                                            new HomTuple<N, V>(
                                                ord2 -> 
                                                    ord1.equalsOrdinal(ord2) ? b : underlyingVectorSpace().zero()))))
                                .toList());
                            return l;
                        }));
    }
}
