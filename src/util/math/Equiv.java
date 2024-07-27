package util.math;

/**
 * Interface to represent an equivalence relation on the given datatype.
 */
public interface Equiv<A> {
    
    /**
     * Represents equality between objects of type A. Obviously this must satisfy the normal conditions
     * for an equivalence relation on A:
     * 
     * <ul>
     *   <li>Reflexivity - {@code a = a}</li>
     *   <li>Symmetry - {@code a = b} implies {@code b = a}</li>
     *   <li>Transitivity - {@code a = b} and {@code b = c} implies {@code a = c}</li>
     * </ul>
     * 
     * @param a1 the first value
     * @param a2 the second value
     * @return whether the two values are equivalent
     */
    public boolean equiv(final A a1, final A a2);
}
