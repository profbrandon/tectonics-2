package util.counting;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import util.counting.Ordinals.Zero;
import util.counting.Ordinals.Five;
import util.counting.Ordinals.Four;
import util.counting.Ordinals.One;
import util.counting.Ordinals.Two;
import util.counting.Ordinals.Three;
import util.data.algebraic.Sum;

public class OrdinalSet<O extends Ordinal> {

    // { 0 }
    public static final OrdinalSet<One> ZERO_1 = inject(Zero.INSTANCE);

    public static final Collection<OrdinalSet<One>> ONE_SET = List.of(ZERO_1);

    // { 0, 1 }
    public static final OrdinalSet<Two> ZERO_2 = lift(ZERO_1);
    public static final OrdinalSet<Two> ONE_2  = inject(One.INSTANCE);

    public static final Collection<OrdinalSet<Two>> TWO_SET = List.of(ZERO_2, ONE_2);

    // { 0, 1, 2 }
    public static final OrdinalSet<Three> ZERO_3 = lift(ZERO_2);
    public static final OrdinalSet<Three> ONE_3  = lift(ONE_2);
    public static final OrdinalSet<Three> TWO_3  = inject(Two.INSTANCE);
    
    public static final Collection<OrdinalSet<Three>> THREE_SET = List.of(ZERO_3, ONE_3, TWO_3);

    // { 0, 1, 2, 3 }
    public static final OrdinalSet<Four> ZERO_4  = lift(ZERO_3);
    public static final OrdinalSet<Four> ONE_4   = lift(ONE_3);
    public static final OrdinalSet<Four> TWO_4   = lift(TWO_3);
    public static final OrdinalSet<Four> THREE_4 = inject(Three.INSTANCE);

    public static final Collection<OrdinalSet<Four>> FOUR_SET = List.of(ZERO_4, ONE_4, TWO_4, THREE_4);

    // { 0, 1, 2, 3, 4 }
    public static final OrdinalSet<Five> ZERO_5  = lift(ZERO_4);
    public static final OrdinalSet<Five> ONE_5   = lift(ONE_4);
    public static final OrdinalSet<Five> TWO_5   = lift(TWO_4);
    public static final OrdinalSet<Five> THREE_5 = lift(THREE_4);
    public static final OrdinalSet<Five> FOUR_5  = inject(Four.INSTANCE);

    public static final Collection<OrdinalSet<Five>> FIVE_SET = List.of(ZERO_5, ONE_5, TWO_5, THREE_5, FOUR_5);

    private final Sum<Prev<O>, OrdinalSet<? extends Prev<O>>> ordinal;

    private OrdinalSet(final Prev<O> ordinal) {
        this.ordinal = Sum.left(ordinal);
    }

    private OrdinalSet(final OrdinalSet<? extends Prev<O>> prevOrdinal) {
        this.ordinal = Sum.right(prevOrdinal);
    }

    private static <O extends Ordinal> OrdinalSet<O> inject(final Prev<O> value) {
        return new OrdinalSet<>(value);
    }

    private static <O extends Ordinal, P extends Prev<O>> OrdinalSet<O> lift(final OrdinalSet<P> value) {
        return new OrdinalSet<O>(value.ordinal.match(
            prev -> new OrdinalSet<>(prev),
            prevOrdSet -> new OrdinalSet<>(prevOrdSet)));
    }

    private static <A> Function<OrdinalSet<One>, A> injectOne(final A value) {
        return ord -> value;
    }

    private static <O extends Ordinal, A> Function<OrdinalSet<O>, A> liftFun(final A higherValue, final Function<OrdinalSet<? extends Prev<O>>, A> lowerValues) {
        return 
            higherOrd -> 
                higherOrd.ordinal.match(
                    ord -> higherValue,
                    lowerValues);
    }

    public static <A> Function<OrdinalSet<Zero>, A> zero() {
        return ordSet -> { throw new IllegalStateException("Somehow supplied an object of type OrdinalSet<Zero>."); };
    }

    public static <A> Function<OrdinalSet<One>, A> one(final A value) {
        return injectOne(value);
    }

    public static <A> Function<OrdinalSet<Two>, A> twoHomo(final A value0, final A value1) {
        return liftFun(value1, ordQ -> one(value0).apply(ZERO_1));
    }

    public static <A> Function<OrdinalSet<Three>, A> threeHomo(final A value0, final A value1, final A value2) {
        return liftFun(value2, ordQ -> twoHomo(value0, value1).apply(
            ordQ.ordinal.match(
                v -> ONE_2,
                v -> ZERO_2)));
    }

    public static <A> Function<OrdinalSet<Four>, A> fourHomo(final A value0, final A value1, final A value2, final A value3) {
        return liftFun(value3, ordQ -> threeHomo(value0, value1, value2).apply(
            ordQ.ordinal.match(
                v -> TWO_3,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> ONE_3,
                    v -> ZERO_3))));
    }

    public static <A> Function<OrdinalSet<Five>, A> fiveHomo(final A value0, final A value1, final A value2, final A value3, final A value4) {
        return liftFun(value4, ordQ -> fourHomo(value0, value1, value2, value3).apply(
            ordQ.ordinal.match(
                v -> THREE_4,
                ordQ1 -> ordQ1.ordinal.match(
                    v -> TWO_4,
                    ordQ2 -> ordQ2.ordinal.match(
                        v -> ONE_4,
                        v -> ZERO_4)))));
    }

    public static <O extends Ordinal, A> Function<OrdinalSet<O>, A> populate(final A value) {
        return ordSet -> value;
    }
}
