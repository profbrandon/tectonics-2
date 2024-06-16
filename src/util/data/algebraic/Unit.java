package util.data.algebraic;

import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent the algebraic unit type (the terminal object). The recursor simply ignores
 * the presence of the {@link Unit#unit()} value.
 */
public final class Unit {

    private static Unit singletonInstance = new Unit();
 
    private Unit() {

    }

    /**
     * Ignores this {@link Unit} value.
     * 
     * @param <A> the type to return
     * @param value the value to return
     * @return the value
     * @throws {@link IllegalArgumentException} when given a {@code null} object
     */
    public final <A> A destroy(final A value) {
        Preconditions.throwIfNull(value, "value");
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the {@link Unit} singleton
     */
    public final static Unit unit() {
        return singletonInstance;
    }

    /**
     * Ignores the given {@link Unit} value.
     * 
     * @param <A> the type to return
     * @param unit the {@link Unit} to ignore
     * @param value the value to return
     * @return the value
     * @throws {@link IllegalArgumentException} when given {@code null} objects
     */
    public final static <A> A destroy(final Unit unit, final A value) {
        Preconditions.throwIfNull(unit, "unit");
        return unit.destroy(value);
    }

    /**
     * @param <A> the type to construct a function from
     * @return a function from an arbitrary type to the {@link Unit} type
     */
    public final static <A> Function<A, Unit> terminal() {
        return a -> Unit.unit();
    }
}
