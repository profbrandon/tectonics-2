package util.math;

/**
 * Interface to represent a mathematical group over the given datatype. Every {@link Group}
 * is a {@link Monoid} with an additional requirement: that every element has an (additive)
 * inverse. For every {@code g}, the inverse {@code -g} must exist and satisfy {@code g + (-g) = 0}.
 */
public interface Group<G> extends Monoid<G> {

    /**
     * Creates the inverse of the given object. Given {@code g}, the negation {@code -g} should
     * satisfy {@code g + (-g) = 0}.
     * 
     * @param g the object to invert
     * @return the inverse of the given object
     */
    public G neg(final G g);
}
