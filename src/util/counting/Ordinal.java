package util.counting;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import util.counting.Cardinals.Zero;
import util.Preconditions;
import util.counting.Cardinals.Five;
import util.counting.Cardinals.Four;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Three;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;

/**
 * Class to represent ordinal numbers, meaning elements of sets like { }, { 0th }, 
 * { 0th, 1st }, etc. In other words, they are elements of sets with the given 
 * cardinality of their {@link Cardinal}. Their primary use is for types that have 
 * some type-dependence on a finite collection of items (Pi-types over the naturals).
 */
public class Ordinal<N extends Cardinal> {

    /**
     * The empty set { }.
     */
    public static final Collection<Ordinal<Zero>> ZERO_SET = List.of();

    /**
     * The 0th element of the singleton set { 0 }.
     */
    public static final Ordinal<One> ZERO_1 = inject(Zero.INSTANCE);

    /**
     * The singleton set { 0 }.
     */
    public static final Collection<Ordinal<One>> ONE_SET = List.of(ZERO_1);

    /**
     * The 0th element of the 2-set { 0, 1 }.
     */
    public static final Ordinal<Two> ZERO_2 = lift(ZERO_1);

    /**
     * The 1st element of the 2-set { 0, 1 }.
     */
    public static final Ordinal<Two> ONE_2  = inject(One.INSTANCE);

    /**
     * The 2-set { 0, 1 }.
     */
    public static final Collection<Ordinal<Two>> TWO_SET = List.of(ZERO_2, ONE_2);

    /**
     * The 0th element of the 3-set { 0, 1, 2 }.
     */
    public static final Ordinal<Three> ZERO_3 = lift(ZERO_2);

    /**
     * The 1st element of the 3-set { 0, 1, 2 }.
     */
    public static final Ordinal<Three> ONE_3  = lift(ONE_2);

    /**
     * The 2nd element of the 3-set { 0, 1, 2 }.
     */
    public static final Ordinal<Three> TWO_3  = inject(Two.INSTANCE);
    
    /**
     * The 3-set { 0, 1, 2 }.
     */
    public static final Collection<Ordinal<Three>> THREE_SET = List.of(ZERO_3, ONE_3, TWO_3);

    /**
     * The 0th element of the 4-set { 0, 1, 2, 3 }.
     */
    public static final Ordinal<Four> ZERO_4  = lift(ZERO_3);

    /**
     * The 1st element of the 4-set { 0, 1, 2, 3 }.
     */
    public static final Ordinal<Four> ONE_4   = lift(ONE_3);

    /**
     * The 2nd element of the 4-set { 0, 1, 2, 3 }.
     */
    public static final Ordinal<Four> TWO_4   = lift(TWO_3);

    /**
     * The 3rd element of the 4-set { 0, 1, 2, 3 }.
     */
    public static final Ordinal<Four> THREE_4 = inject(Three.INSTANCE);

    /**
     * The 4-set { 0, 1, 2, 3 }.
     */
    public static final Collection<Ordinal<Four>> FOUR_SET = List.of(ZERO_4, ONE_4, TWO_4, THREE_4);

    /**
     * The 0th element of the 5-set { 0, 1, 2, 3, 4 }.
     */
    public static final Ordinal<Five> ZERO_5  = lift(ZERO_4);

    /**
     * The 1st element of the 5-set { 0, 1, 2, 3, 4 }.
     */
    public static final Ordinal<Five> ONE_5   = lift(ONE_4);

    /**
     * The 2nd element of the 5-set { 0, 1, 2, 3, 4 }.
     */
    public static final Ordinal<Five> TWO_5   = lift(TWO_4);

    /**
     * The 3rd element of the 5-set { 0, 1, 2, 3, 4 }.
     */
    public static final Ordinal<Five> THREE_5 = lift(THREE_4);

    /**
     * The 4th element of the 5-set { 0, 1, 2, 3, 4 }.
     */
    public static final Ordinal<Five> FOUR_5  = inject(Four.INSTANCE);

    /**
     * The 5-set { 0, 1, 2, 3, 4, 5 }.
     */
    public static final Collection<Ordinal<Five>> FIVE_SET = List.of(
        ZERO_5, 
        ONE_5, 
        TWO_5, 
        THREE_5, 
        FOUR_5);

    private final Sum<Pred<N>, Ordinal<? extends Pred<N>>> ordinal;

    private Ordinal(final Pred<N> ordinal) {
        this.ordinal = Sum.left(ordinal);
    }

    private Ordinal(final Ordinal<? extends Pred<N>> prevOrdinal) {
        this.ordinal = Sum.right(prevOrdinal);
    }

    public Cardinal underlyingCardinal() {
        return this.ordinal.match(
            prev -> prev,
            ord  -> ord.underlyingCardinal());
    }

    private static <N extends Cardinal> Ordinal<N> inject(final Pred<N> value) {
        Preconditions.throwIfNull(value, "value");
        return new Ordinal<>(value);
    }

    private static <N extends Cardinal, P extends Pred<N>> Ordinal<N> lift(final Ordinal<P> value) {
        Preconditions.throwIfNull(value, "value");
        return new Ordinal<N>((Ordinal<P>) value.ordinal.match(
            prev -> new Ordinal<P>(prev),
            prevOrdSet -> new Ordinal<P>(prevOrdSet)));
    }

    private static <A> Function<Ordinal<One>, A> injectOne(final A value) {
        Preconditions.throwIfNull(value, "value");
        return ord -> value;
    }

    private static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        liftFun(
            final A higherValue, 
            final Function<Ordinal<? extends Pred<N>>, A> lowerValues) {

        Preconditions.throwIfNull(higherValue, "higherValue");
        Preconditions.throwIfNull(lowerValues, "lowerValues");
        return 
            higherOrd -> 
                higherOrd.ordinal.match(
                    ord -> higherValue,
                    lowerValues);
    }

    /**
     * Determines if this ordinal is equal to the given one. Note that this can only check for
     * equality between two ordinals of the same {@link Cardinal}.
     * 
     * @param other the other ordinal
     * @return whether the ordinals are equal
     */
    public boolean equalsOrdinal(final Ordinal<N> other) {
        Preconditions.throwIfNull(other, "other");
        return this.underlyingCardinal().getInteger() == other.underlyingCardinal().getInteger();
    }

    /**
     * Determines if this ordinal is less than or equal to the given one. Note that this can
     * only compare ordinals of the same {@link Cardinal}.
     * 
     * @param other the other ordinal
     * @return whether this ordinal is less than or equal to the given one
     */
    public boolean lessThanEqualOrdinal(final Ordinal<N> other) {
        Preconditions.throwIfNull(other, "other");
        return this.underlyingCardinal().getInteger() <= other.underlyingCardinal().getInteger();
    }

    /**
     * Determines if this ordinal is loosely equal to the given one. This means that if they
     * both represent the same elemental order (0th, 1st, 2nd, 3rd, etc.) but does not care about
     * the overlying set.
     * 
     * @param <M> the cardinal of the overlying set of the other ordinal
     * @param other the other ordinal
     * @return whether these two ordinals are loosely equal
     */
    public <M extends Cardinal> boolean equalsOrdinalLoose(final Ordinal<M> other) {
        Preconditions.throwIfNull(other, "other");
        return this.underlyingCardinal().getInteger() == other.underlyingCardinal().getInteger();
    }

    /**
     * Creates an indexing function that maps elements of the 0-set to elements of a type.
     * 
     * @param <A> the target type
     * @return a function from the zero ordinal to the target type
     */
    public static <A> Function<Ordinal<Zero>, A> zero() {
        return ordSet -> { 
            throw new IllegalStateException("Somehow supplied an object of type OrdinalSet<Zero>."); 
        };
    }

    /**
     * Creates an indexing function that maps elements of the 1-set to elements of a type.
     * 
     * @param <A> the target type
     * @param value the target value for {@link Ordinal#ZERO_1}
     * @return a function from the 1-set to the provided value
     */
    public static <A> Function<Ordinal<One>, A> one(final A value) {
        return injectOne(value);
    }

    /**
     * Creates an indexing function that maps elements of the 2-set to elements of a type.
     * 
     * @param <A> the target type
     * @param value0 the target value for {@link Ordinal#ZERO_2}
     * @param value1 the target value for {@link Ordinal#ONE_2}
     * @return a function from the 2-set to the provided type
     */
    public static <A> Function<Ordinal<Two>, A> twoHomo(final A value0, final A value1) {
        Preconditions.throwIfNull(value0, "value0");
        return liftFun(value1, ordQ -> one(value0).apply(ZERO_1));
    }

    /**
     * Creates an indexing function that maps elements of the 3-set to elements of a type.
     * 
     * @param <A> the target type
     * @param value0 the target value for {@link Ordinal#ZERO_3}
     * @param value1 the target value for {@link Ordinal#ONE_3}
     * @param value2 the target value for {@link Ordinal#TWO_3}
     * @return a function from the 3-set to the provided type
     */
    public static <A> Function<Ordinal<Three>, A> 
        threeHomo(
            final A value0, 
            final A value1, 
            final A value2) {

        Preconditions.throwIfNull(value0, "value0");
        Preconditions.throwIfNull(value1, "value1");
        return liftFun(value2, ordQ -> twoHomo(value0, value1).apply(
            ordQ.ordinal.match(
                v -> ONE_2,
                v -> ZERO_2)));
    }

    /**
     * Creates an indexing function that maps elements of the 4-set to elements of a type.
     * 
     * @param <A> the target type
     * @param value0 the target value for {@link Ordinal#ZERO_4}
     * @param value1 the target value for {@link Ordinal#ONE_4}
     * @param value2 the target value for {@link Ordinal#TWO_4}
     * @param value3 the target value for {@link Ordinal#THREE_4}
     * @return a function from the 4-set to the provided type
     */
    public static <A> Function<Ordinal<Four>, A> 
        fourHomo(
            final A value0, 
            final A value1, 
            final A value2, 
            final A value3) {

        Preconditions.throwIfNull(value0, "value0");
        Preconditions.throwIfNull(value1, "value1");
        Preconditions.throwIfNull(value2, "value2");
        return liftFun(value3, ordQ -> threeHomo(value0, value1, value2).apply(
            ordQ.ordinal.match(
                v -> TWO_3,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> ONE_3,
                    v -> ZERO_3))));
    }

    /**
     * Creates an indexing function that maps elements of the 5-set to elements of a type.
     * 
     * @param <A> the target type
     * @param value0 the target value for {@link Ordinal#ZERO_5}
     * @param value1 the target value for {@link Ordinal#ONE_5}
     * @param value2 the target value for {@link Ordinal#TWO_5}
     * @param value3 the target value for {@link Ordinal#THREE_5}
     * @param value4 the target value for {@link Ordinal#FOUR_5}
     * @return a function from the 5-set to the provided type
     */
    public static <A> Function<Ordinal<Five>, A> 
        fiveHomo(
            final A value0, 
            final A value1, 
            final A value2, 
            final A value3, 
            final A value4) {

        Preconditions.throwIfNull(value0, "value0");
        Preconditions.throwIfNull(value1, "value1");
        Preconditions.throwIfNull(value2, "value2");
        Preconditions.throwIfNull(value3, "value3");
        return liftFun(value4, ordQ -> fourHomo(value0, value1, value2, value3).apply(
            ordQ.ordinal.match(
                v -> THREE_4,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> TWO_4,
                    ordQ2 -> ordQ2.ordinal.match(
                        v -> ONE_4,
                        v -> ZERO_4)))));
    }

    /**
     * Creates a constant function that returns the given value upon all provided ordinals.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the target value's type
     * @param value the target value
     * @return a constant function that returns the given value
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> populate(final A value) {
        Preconditions.throwIfNull(value, "value");
        return ordSet -> value;
    }

    /**
     * Creates a function that partitions the ordinals into two groups: below or equal to some 
     * {@link Ordinal} or above it. Each group is mapped to their own partition.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the type to map to
     * @param testOrd the ordinal to test against (i.e., above, below, equal)
     * @param equalOrBelow the value of the lower partition
     * @param above the value of the upper partition
     * @return a partition function
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        partition(
            final Ordinal<N> testOrd, 
            final A equalOrBelow, 
            final A above) {

        Preconditions.throwIfNull(equalOrBelow, "equalOrBelow");
        Preconditions.throwIfNull(above, "above");
        return ord -> ord.lessThanEqualOrdinal(testOrd) ? equalOrBelow : above;
    }

    /**
     * Creates a function in which there are as many partitions as there are distinct elements
     * in the first part of the {@code enumerated}. If there are as many distinct elements as 
     * the cardinality {@code N}, then enumerated builds a function in which each ordinal is 
     * mapped to the corresponding element of {@code A}. In that case, it acts exactly like 
     * converting a map or "dictionary" to a function.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the output type
     * @param enumerated the enumerated pairs of items
     * @return
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        buildPartition(
            final List<Prod<Ordinal<N>, A>> enumerated) {

        Preconditions.throwIfContainsNull(enumerated, "enumerated");

        if (enumerated.size() == 1) {
            return populate(enumerated.get(0).second());
        } else {
            return ord -> partition(
                enumerated.get(0).first(), 
                enumerated.get(0).second(), 
                buildPartition(enumerated.stream().skip(1).toList()).apply(ord)).apply(ord);
        }
    }

    /**
     * Create an ordinal map that outputs a value on a specific ordinal and another value on 
     * every other ordinal, i.e.,
     * 
     * <ul>
     *   <li>{@code only(ord, x, y)(ord) == x}</li>
     *   <li>{@code only(ord, x, y)(other) == y} for any {@code other != ord}</li>
     * </ul>
     * 
     * @param <N> the cardinality of the input
     * @param <A> the target value
     * @param ord the ordinal to specifically map to a particular object
     * @param onOrdinal the object to map to on the specified ordinal
     * @param otherwise the object to map to on every other oridinal
     * @return a map satisfying the above
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        only(
            final Ordinal<N> ord, 
            final A onOrdinal, 
            final A otherwise) {

        Preconditions.throwIfNull(ord, "ord");
        Preconditions.throwIfNull(onOrdinal, "onOrdinal");
        Preconditions.throwIfNull(otherwise, "otherwise");

        return otherOrd -> ord.equalsOrdinal(otherOrd) ? onOrdinal : otherwise;
    }

    /**
     * Replaces the element at the specified index with the given one.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the output type
     * @param fun the indexing function on which the replacement will be done
     * @param ord the ordinal index that will be remapped
     * @param onOrdinal the value that will be placed at the given ordinal
     * @return a new indexing function
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        replace(
            final Function<Ordinal<N>, A> fun, 
            final Ordinal<N> ord, 
            final A onOrdinal) {

        Preconditions.throwIfNull(fun, "fun");
        return x -> only(ord, onOrdinal, fun.apply(x)).apply(x);
    }

    /**
     * Swaps the elements at the specified indices.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the output type
     * @param fun a function whose indices will be swapped
     * @param ord1 the first ordinal
     * @param ord2 the second ordinal
     * @return an indexing function with the given elements swapped
     */
    public static <N extends Cardinal, A> Function<Ordinal<N>, A> 
        swap(
            final Function<Ordinal<N>, A> fun, 
            final Ordinal<N> ord1, 
            final Ordinal<N> ord2) {

        Preconditions.throwIfNull(fun, "fun");
        return fun.compose(
            replace(
                replace(
                    x -> x, 
                    ord2, 
                    ord1), 
                ord1, 
                ord2));
    }

    /**
     * Drops the last index.
     * 
     * @param <N> the cardinality of the input
     * @param <A> the type of element to index over
     * @param fun the initial indexing function
     * @return an indexing function with the last index dropped
     */
    public static <N extends Cardinal, A> Function<Ordinal<? extends Pred<N>>, A> 
        dropLast(
            final Function<Ordinal<N>, A> fun) {

        Preconditions.throwIfNull(fun, "fun");
        return pred -> fun.apply(lift(pred));
    }
}
