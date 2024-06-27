package util.data.algebraic;

import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic empty type (the initial object). The recursor cannot, in reality,
 * be called since it would require instantiating the {@link Empty} type.
 */
public final class Empty {
    
    /**
     * Private constructor to disallow creation.
     */
    private Empty() {

    }

    /**
     * Absurdity, since it cannot possibly be called.
     * 
     * @param <U> the type to spontaneously generate
     * @param empty the vacuous value to supply
     * @return an impossibly generated element of the resultant type
     */
    public static <U> U absurd(final Empty empty) {
        Preconditions.throwIfNull(empty, "empty");
        return null;
    }

    /**
     * @param <A> the type to construct a function to
     * @return the initial function from the {@link Empty} type to the resultant type
     */
    public static <A> Function<Empty, A> initial() {
        return Empty::absurd;
    }
}
