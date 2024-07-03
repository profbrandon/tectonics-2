package util.math;

public interface Equiv<A> {
    
    /**
     * Represents equality between objects of type A. Obviously this must satisfy the normal conditions
     * for an equivalence relation on A.
     * 
     * @param a1 the first value
     * @param a2 the second value
     * @return whether the two values are equivalent
     */
    public boolean equiv(final A a1, final A a2);
}
