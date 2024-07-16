package util.math.vectorspaces.finite;

import java.util.List;

import util.data.algebraic.Prod;
import util.math.vectorspaces.ProductSpace;

/**
 * Class to represent a finite-dimensional version of {@link ProductSpace}. The basis of this
 * space is generated via the underlying vector space's basis as cartesian products of the
 * underlying basis vectors with each vector space's zero element.
 */
public class FiniteProductSpace<V, W, K>
    extends
        ProductSpace<V, W, K>
    implements
        FiniteVectorSpace<Prod<V, W>, K> {

    private final FiniteVectorSpace<V, K> LEFT_SPACE;
    private final FiniteVectorSpace<W, K> RIGHT_SPACE;

    /**
     * Constructs a finite-dimensional product space of the two vector spaces. (They must be
     * over the same field.)
     * 
     * @param leftVectorSpace the left finite-dimensional vector space
     * @param rightVectorSpace the right finite-dimensional vector space
     */
    public FiniteProductSpace(final FiniteVectorSpace<V, K> leftVectorSpace, final FiniteVectorSpace<W, K> rightVectorSpace) {
        super(leftVectorSpace, rightVectorSpace);

        this.LEFT_SPACE = leftVectorSpace;
        this.RIGHT_SPACE = rightVectorSpace;
    }

    @Override
    public FiniteVectorSpace<V, K> leftSpace() {
        return this.LEFT_SPACE;
    }

    @Override
    public FiniteVectorSpace<W, K> rightSpace() {
        return this.RIGHT_SPACE;
    }

    @Override
    public List<Prod<V, W>> basis() {
        return leftSpace()
            .basis()
            .stream()
            .flatMap(l -> rightSpace()
                .basis()
                .stream()
                .map(r -> Prod.pair(l, r)))
            .toList();
    }

    @Override
    public List<Prod<K, Prod<V, W>>> decompose(final Prod<V, W> pair) {
        return pair.destroy(
            v -> w -> leftSpace()
                .decompose(v)
                .stream()
                .flatMap(kb1 -> kb1.destroy(
                    k1 -> b1 -> rightSpace()
                        .decompose(w)
                        .stream()
                        .map(kb2 -> kb2.destroy(
                            k2 -> b2 -> Prod.pair(
                                underlyingField().mult(k1, k2), 
                                Prod.pair(b1, b2))))))
                .toList());
    }
}
