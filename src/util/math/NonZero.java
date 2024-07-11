package util.math;

import util.Preconditions;

/**
 * Class to represent a nonzero value of a {@link Monoid}.
 */
public class NonZero<A> {

    private final A VALUE;

    /**
     * Wraps the value in the event that it is not the {@link Monoid#zero()}.
     * 
     * @param a the value to wrap
     * @param monoid the {@link Monoid} to check against
     */
    public NonZero(final A value, final Monoid<A> monoid) {
        Preconditions.throwIfNull(value, "value");
        Preconditions.throwIfNull(monoid, "monoid");
        Preconditions.throwIfEquals(value, monoid.zero(), "Cannot instantiate a nonzero value with the zero value.");
        this.VALUE = value;
    }

    /**
     * @return the nonzero value
     */
    public A get() {
        return this.VALUE;
    }
}
