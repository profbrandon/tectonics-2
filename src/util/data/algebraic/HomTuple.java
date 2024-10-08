package util.data.algebraic;

import java.util.Collection;
import java.util.function.Function;

import util.Preconditions;
import util.counting.Cardinal;
import util.counting.Ordinal;
import util.counting.Cardinals.Five;
import util.counting.Cardinals.Four;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Three;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Zero;
import util.counting.Cardinals;

/**
 * Class to represent a {@link N}-tuple of homogeneously-typed values where {@link N} is an
 * {@link Cardinal}. For example, a {@link Cardinals.One}-Tuple of characters looks like a singleton:
 * 
 * <p>
 *     {@code ('a')}
 * </p>
 * 
 * and a {@link Cardinals.Two}-Tuple of booleans looks like a pair:
 * 
 * <p>
 *     {@code (true, false)}
 * </p>
 * 
 * Each of the tuples has its own labeled projections via the {@link HomTuple#at(Ordinal)} method.
 */
public class HomTuple<N extends Cardinal, A> {

    private final Function<Ordinal<N>, A> elements;
    
    public HomTuple(final Function<Ordinal<N>, A> elements) {
        Preconditions.throwIfNull(elements, "elements");
        this.elements = elements;
    }

    /**
     * Labeled projections out of this {@link HomTuple}.
     * 
     * @param index the requested ordinal index
     * @return the value at the requested index
     */
    public A at(final Ordinal<N> index) {
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
    public <B> HomTuple<N, B> mapAll(final Function<A, B> function) {
        Preconditions.throwIfNull(function, "function");
        return new HomTuple<>(ordSet -> function.apply(this.elements.apply(ordSet)));
    }

    /**
     * Method to map all elements of this tuple via a index-dependent function.
     * 
     * @param <B> the type to convert to
     * @param enumFuns an {@link Cardinal}-indexed collection of functions
     * @return the mapped tuple
     */
    public <B> HomTuple<N, B> mapEach(final Function<Ordinal<N>, Function<A, B>> enumFuns) {
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
    public <B> HomTuple<N, B> mapEach(final HomTuple<N, Function<A, B>> funTuple) {
        Preconditions.throwIfNull(funTuple, "funTuple");
        return this.mapEach(funTuple.elements);
    }

    /**
     * Maps this {@link HomTuple} to some other type by providing a seed and a folding function.
     * 
     * @param <U> the type to map to (accumulator)
     * @param enumerated the ordinal tags of the elmenets to accumulate
     * @param seed the seed value for the accumulator
     * @param reduce the accumulator function
     * @return the reduced value
     */
    public <U> U eliminate(final Collection<Ordinal<N>> enumerated, final U seed, final Function<Ordinal<N>, Function<Prod<U, A>, U>> reduce) {
        U value = seed;
        
        for (final Ordinal<N> ord : enumerated) {
            value = reduce.apply(ord).apply(Prod.pair(value, this.at(ord)));    
        }

        return value;
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
    public <B> HomTuple<N, Prod<A, B>> zip(final HomTuple<N, B> other) {
        Preconditions.throwIfNull(other, "other");
        return new HomTuple<>(ord -> Prod.pair(this.at(ord), other.at(ord)));
    }

    /**
     * Swaps the elements at the given indices.
     * 
     * @param ord1 the first ordinal
     * @param ord2 the second ordinal
     * @return a new {@link HomTuple} with the elements at the specified indices exchanged
     */
    public HomTuple<N, A> swap(final Ordinal<N> ord1, final Ordinal<N> ord2) {
        return new HomTuple<>(Ordinal.swap(elements, ord1, ord2));
    }

    /**
     * Tests the provided {@link HomTuple} for equality against this one. Note that it only checks the
     * provided indices in the {@link Ordinal} collection argument. The underlying type is checked
     * for equality via the {@link Object#equals(Object)} method.
     * 
     * @param enumerated the collection of indices to check for equality
     * @param other the other {@link HomTuple}
     * @return whether these two objects are equivalent (up to the given indices)
     */
    public boolean equalsTuple(final Collection<Ordinal<N>> enumerated, final HomTuple<N, A> other) {
        return this.equalsTuple(pair -> pair.destroy(a -> b -> a.equals(b)), enumerated, other);
    }

    /**
     * Tests the provided {@link HomTuple} for equality against this one. Note that it only checks the
     * provided indices in the {@link Ordinal} collection argument. The underlying type is checked
     * for equality via the provided equality function.
     * 
     * @param equality an equality function (should be a true equivalence)
     * @param enumerated the collection of indices to check for equality
     * @param other the other {@link HomTuple}
     * @return whether these two objects are equivalent (up to the given indices and provided equivalence)
     */
    public boolean equalsTuple(
        final Function<Prod<A, A>, Boolean> equality,
        final Collection<Ordinal<N>> enumerated,
        final HomTuple<N, A> other) {

        Preconditions.throwIfNull(equality, "equality");
        Preconditions.throwIfContainsNull(enumerated, "enumerated");
        Preconditions.throwIfNull(other, "other");

        for (final Ordinal<N> ord : enumerated) {
            if (!equality.apply(Prod.pair(this.at(ord),(other.at(ord))))) return false;
        }
        return true;
    }

    /**
     * Converts a {@link HomTuple} to a string. Note that this only outputs elements of the entire tuple if the
     * provided {@link Collection} of {@link Ordinal} objects is exhaustive. 
     * 
     * @param <N> the provided {@link HomTuple}'s dimension
     * @param <V> the type of the underlying data
     * @param enumerated the collection of {@link Ordinal} objects
     * @param tuple the tuple to represent as a string
     * @return a string representation of the tuple
     */
    public static <N extends Cardinal, V> String toString(final Collection<Ordinal<N>> enumerated, final HomTuple<N, V> tuple) {
        Preconditions.throwIfNull(enumerated, "enumerated");
        Preconditions.throwIfNull(tuple, "tuple");

        final Function<String, String> wrapper = v -> "(" + v + ")";

        return wrapper.apply(enumerated.stream().map(ord -> tuple.at(ord).toString()).reduce((a, b) -> a + ", " + b).get());
    }

    public static <N extends Cardinal, A> HomTuple<N, A> all(final A value) {
        return new HomTuple<>(Ordinal.populate(value));
    }

    public static <N extends Cardinal, A> HomTuple<N, A> only(final Ordinal<N> ord, final A value, final A otherwise) {
        return new HomTuple<>(Ordinal.only(ord, value, otherwise));
    }

    public static <A> HomTuple<Zero, A> tuple() {
        return new HomTuple<>(Ordinal.zero());
    }

    public static <A> HomTuple<One, A> tuple(final A value) {
        return new HomTuple<>(Ordinal.one(value));
    }

    public static <A> HomTuple<Two, A> tuple(final A v0, final A v1) {
        return new HomTuple<>(Ordinal.twoHomo(v0, v1));
    }

    public static <A> HomTuple<Three, A> tuple(final A v0, final A v1, final A v2) {
        return new HomTuple<>(Ordinal.threeHomo(v0, v1, v2));
    }

    public static <A> HomTuple<Four, A> tuple(final A v0, final A v1, final A v2, final A v3) {
        return new HomTuple<>(Ordinal.fourHomo(v0, v1, v2, v3));
    }

    public static <A> HomTuple<Five, A> tuple(final A v0, final A v1, final A v2, final A v3, final A v4) {
        return new HomTuple<>(Ordinal.fiveHomo(v0, v1, v2, v3, v4));
    }

    public static <A> Prod<A, A> toProd(final HomTuple<Two, A> tuple) {
        return Prod.pair(tuple.at(Ordinal.ZERO_2), tuple.at(Ordinal.ONE_2));
    }
}
