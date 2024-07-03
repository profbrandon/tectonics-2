package util.data.algebraic;

import java.util.Collection;
import java.util.function.Function;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.OrdinalSet;
import util.counting.Ordinals;

/**
 * Class to represent a {@link O}-tuple of homogeneously-typed values where {@link O} is an
 * {@link Ordinal}. For example, a {@link Ordinals.One}-Tuple of characters looks like a singleton:
 * 
 * <p>
 *     {@code ('a')}
 * </p>
 * 
 * and a {@link Ordinals.Two}-Tuple of booleans looks like a pair:
 * 
 * <p>
 *     {@code (true, false)}
 * </p>
 * 
 * Each of the tuples has its own labeled projections via the {@link HomTuple#at(OrdinalSet)} method.
 */
public class HomTuple<O extends Ordinal, A> {

    private final Function<OrdinalSet<O>, A> elements;
    
    public HomTuple(final Function<OrdinalSet<O>, A> elements) {
        Preconditions.throwIfNull(elements, "elements");
        this.elements = elements;
    }

    /**
     * Labeled projections out of this {@link HomTuple}.
     * 
     * @param index the requested ordinal index
     * @return the value at the requested index
     */
    public A at(final OrdinalSet<O> index) {
        Preconditions.throwIfNull(index, "index");
        return this.elements.apply(index);
    }

    /**
     * Method to map all elements of this tuple via a uniform (index-independent) function.
     * 
     * @param <B> the type to convert to
     * @param function the index-independent function
     * @return the mapped tuple
     */
    public <B> HomTuple<O, B> mapAll(final Function<A, B> function) {
        Preconditions.throwIfNull(function, "function");
        return new HomTuple<>(ordSet -> function.apply(this.elements.apply(ordSet)));
    }

    /**
     * Method to map all elements of this tuple via a index-dependent function.
     * 
     * @param <B> the type to convert to
     * @param enumFuns an {@link Ordinal}-indexed collection of functions
     * @return the mapped tuple
     */
    public <B> HomTuple<O, B> mapEach(final Function<OrdinalSet<O>, Function<A, B>> enumFuns) {
        Preconditions.throwIfNull(enumFuns, "enumFuns");
        return new HomTuple<>(
            ordSet -> 
                enumFuns
                    .apply(ordSet) // Extract nth function
                    .apply(this.at(ordSet))); // Extract nth value and feed it to nth function
    }

    /**
     * Method to map all elements of this tuple via a tuple of functions.
     * 
     * @param <B> the type to convert to
     * @param funTuple a tuple of functions
     * @return the mapped tuple
     */
    public <B> HomTuple<O, B> mapEach(final HomTuple<O, Function<A, B>> funTuple) {
        Preconditions.throwIfNull(funTuple, "funTuple");
        return this.mapEach(funTuple.elements);
    }

    /**
     * Zips the provided {@link HomTuple} together with this one to create a {@link HomTuple} of
     * {@link Prod} objects in the obvious way.<p/>
     * 
     * Example:<p/>
     * 
     * {@code ('a', 'b').zip((true, false)) -> (<'a', true>, <'b', false>)}
     * 
     * <p/>
     * where the angle-brackets denote the {@link Prod#pair(Object, Object)} operation.
     * 
     * @param <B> the value type of the other tuple
     * @param other the other tuple of the same ordinal
     * @return the zipped tuple
     */
    public <B> HomTuple<O, Prod<A, B>> zip(final HomTuple<O, B> other) {
        Preconditions.throwIfNull(other, "other");
        return new HomTuple<>(ord -> Prod.pair(this.at(ord), other.at(ord)));
    }

    /**
     * Tests the provided {@link HomTuple} for equality against this one. Note that it only checks the
     * provided indices in the {@link OrdinalSet} collection argument. The underlying type is checked
     * for equality via the {@link Object#equals(Object)} method.
     * 
     * @param enumerated the collection of indices to check for equality
     * @param other the other {@link HomTuple}
     * @return whether these two objects are equivalent (up to the given indices)
     */
    public boolean equalsTuple(final Collection<OrdinalSet<O>> enumerated, final HomTuple<O, A> other) {
        return this.equalsTuple(pair -> pair.destroy(a -> b -> a.equals(b)), enumerated, other);
    }

    /**
     * Tests the provided {@link HomTuple} for equality against this one. Note that it only checks the
     * provided indices in the {@link OrdinalSet} collection argument. The underlying type is checked
     * for equality via the provided equality function.
     * 
     * @param equality an equality function (should be a true equivalence)
     * @param enumerated the collection of indices to check for equality
     * @param other the other {@link HomTuple}
     * @return whether these two objects are equivalent (up to the given indices and provided equivalence)
     */
    public boolean equalsTuple(
        final Function<Prod<A, A>, Boolean> equality,
        final Collection<OrdinalSet<O>> enumerated,
        final HomTuple<O, A> other) {

        Preconditions.throwIfNull(equality, "equality");
        Preconditions.throwIfNull(enumerated, "enumerated");
        Preconditions.throwIfNull(other, "other");

        for (final OrdinalSet<O> ord : enumerated) {
            if (!equality.apply(Prod.pair(this.at(ord),(other.at(ord))))) return false;
        }
        return true;
    }

    /**
     * Converts a {@link HomTuple} to a string. Note that this only outputs elements of the entire tuple if the
     * provided {@link Collection} of {@link OrdinalSet} objects is exhaustive. 
     * 
     * @param <N> the provided {@link HomTuple}'s dimension
     * @param <V> the type of the underlying data
     * @param enumerated the collection of {@link OrdinalSet} objects
     * @param tuple the tuple to represent as a string
     * @return a string representation of the tuple
     */
    public static <N extends Ordinal, V> String toString(final Collection<OrdinalSet<N>> enumerated, final HomTuple<N, V> tuple) {
        Preconditions.throwIfNull(enumerated, "enumerated");
        Preconditions.throwIfNull(tuple, "tuple");

        final Function<String, String> wrapper = v -> "(" + v + ")";

        return wrapper.apply(enumerated.stream().map(ord -> tuple.at(ord).toString()).reduce((a, b) -> a + ", " + b).get());
    }
}
