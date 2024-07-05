package util.counting;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import util.counting.Cardinals.Zero;
import util.counting.Cardinals.Five;
import util.counting.Cardinals.Four;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Three;
import util.data.algebraic.Sum;

public class Ordinal<N extends Cardinal> {

    // { 0 }
    public static final Ordinal<One> ZERO_1 = inject(Zero.INSTANCE);

    public static final Collection<Ordinal<One>> ONE_SET = List.of(ZERO_1);

    // { 0, 1 }
    public static final Ordinal<Two> ZERO_2 = lift(ZERO_1);
    public static final Ordinal<Two> ONE_2  = inject(One.INSTANCE);

    public static final Collection<Ordinal<Two>> TWO_SET = List.of(ZERO_2, ONE_2);

    // { 0, 1, 2 }
    public static final Ordinal<Three> ZERO_3 = lift(ZERO_2);
    public static final Ordinal<Three> ONE_3  = lift(ONE_2);
    public static final Ordinal<Three> TWO_3  = inject(Two.INSTANCE);
    
    public static final Collection<Ordinal<Three>> THREE_SET = List.of(ZERO_3, ONE_3, TWO_3);

    // { 0, 1, 2, 3 }
    public static final Ordinal<Four> ZERO_4  = lift(ZERO_3);
    public static final Ordinal<Four> ONE_4   = lift(ONE_3);
    public static final Ordinal<Four> TWO_4   = lift(TWO_3);
    public static final Ordinal<Four> THREE_4 = inject(Three.INSTANCE);

    public static final Collection<Ordinal<Four>> FOUR_SET = List.of(ZERO_4, ONE_4, TWO_4, THREE_4);

    // { 0, 1, 2, 3, 4 }
    public static final Ordinal<Five> ZERO_5  = lift(ZERO_4);
    public static final Ordinal<Five> ONE_5   = lift(ONE_4);
    public static final Ordinal<Five> TWO_5   = lift(TWO_4);
    public static final Ordinal<Five> THREE_5 = lift(THREE_4);
    public static final Ordinal<Five> FOUR_5  = inject(Four.INSTANCE);

    public static final Collection<Ordinal<Five>> FIVE_SET = List.of(ZERO_5, ONE_5, TWO_5, THREE_5, FOUR_5);

    private final Sum<Prev<N>, Ordinal<? extends Prev<N>>> ordinal;

    private Ordinal(final Prev<N> ordinal) {
        this.ordinal = Sum.left(ordinal);
    }

    private Ordinal(final Ordinal<? extends Prev<N>> prevOrdinal) {
        this.ordinal = Sum.right(prevOrdinal);
    }

    public Cardinal underlyingCardinal() {
        return this.ordinal.match(
            prev -> prev,
            ord  -> ord.underlyingCardinal());
    }

    public boolean equalsOrdinal(final Ordinal<N> other) {
        return this.underlyingCardinal().equals(other.underlyingCardinal());
    }

    private static <N extends Cardinal> Ordinal<N> inject(final Prev<N> value) {
        return new Ordinal<>(value);
    }

    private static <N extends Cardinal, P extends Prev<N>> Ordinal<N> lift(final Ordinal<P> value) {
        return new Ordinal<N>(value.ordinal.match(
            prev -> new Ordinal<>(prev),
            prevOrdSet -> new Ordinal<>(prevOrdSet)));
    }

    private static <A> Function<Ordinal<One>, A> injectOne(final A value) {
        return ord -> value;
    }

    private static <N extends Cardinal, A> Function<Ordinal<N>, A> liftFun(final A higherValue, final Function<Ordinal<? extends Prev<N>>, A> lowerValues) {
        return 
            higherOrd -> 
                higherOrd.ordinal.match(
                    ord -> higherValue,
                    lowerValues);
    }

    public static <A> Function<Ordinal<Zero>, A> zero() {
        return ordSet -> { throw new IllegalStateException("Somehow supplied an object of type OrdinalSet<Zero>."); };
    }

    public static <A> Function<Ordinal<One>, A> one(final A value) {
        return injectOne(value);
    }

    public static <A> Function<Ordinal<Two>, A> twoHomo(final A value0, final A value1) {
        return liftFun(value1, ordQ -> one(value0).apply(ZERO_1));
    }

    public static <A> Function<Ordinal<Three>, A> threeHomo(final A value0, final A value1, final A value2) {
        return liftFun(value2, ordQ -> twoHomo(value0, value1).apply(
            ordQ.ordinal.match(
                v -> ONE_2,
                v -> ZERO_2)));
    }

    public static <A> Function<Ordinal<Four>, A> fourHomo(final A value0, final A value1, final A value2, final A value3) {
        return liftFun(value3, ordQ -> threeHomo(value0, value1, value2).apply(
            ordQ.ordinal.match(
                v -> TWO_3,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> ONE_3,
                    v -> ZERO_3))));
    }

    public static <A> Function<Ordinal<Five>, A> fiveHomo(final A value0, final A value1, final A value2, final A value3, final A value4) {
        return liftFun(value4, ordQ -> fourHomo(value0, value1, value2, value3).apply(
            ordQ.ordinal.match(
                v -> THREE_4,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> TWO_4,
                    ordQ2 -> ordQ2.ordinal.match(
                        v -> ONE_4,
                        v -> ZERO_4)))));
    }

    public static <N extends Cardinal, A> Function<Ordinal<N>, A> populate(final A value) {
        return ordSet -> value;
    }
}
