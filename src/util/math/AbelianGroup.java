package util.math;

/**
 * Interface to represent a mathematical Abelian (commutative) group over the given datatype. 
 * Every {@link AbelianGroup} is a {@link Group} where the addition is commutative, i.e.,
 * {@code g + h = h + g}.
 */
public interface AbelianGroup<G> extends Group<G> {

    /**
     * The group addition should satisfy commutivity: {@code sum(g, h) = sum(h, g)}.
     *
     * @param g1 the first summand
     * @param g2 the second summand
     * @return the resulting abelian group "sum" of the two objects
     */
    public G sum(final G g1, final G g2);
}