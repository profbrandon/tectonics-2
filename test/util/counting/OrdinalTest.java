package test.util.counting;

import java.util.List;
import java.util.function.Function;

import util.counting.Ordinal;
import util.counting.Cardinals.Five;
import util.counting.Cardinals.Four;
import util.counting.Cardinals.Three;
import util.data.algebraic.Prod;
import util.testing.UnitTest;

public class OrdinalTest extends UnitTest {
    
    public OrdinalTest() {
        super("Ordinal Set Test");
    }

    public static void main(final String[] args) {
        final OrdinalTest unitTest = new OrdinalTest();

        unitTest.addTest(OrdinalTest::oneSetEquality);
        unitTest.addTest(OrdinalTest::twoSetEquality);
        unitTest.addTest(OrdinalTest::threeSetEquality);
        unitTest.addTest(OrdinalTest::fourSetEquality);
        unitTest.addTest(OrdinalTest::fiveSetEquality);
        unitTest.addTest(OrdinalTest::lessThanEqualTest);
        unitTest.addTest(OrdinalTest::populateTest);
        unitTest.addTest(OrdinalTest::partitionTest);
        unitTest.addTest(OrdinalTest::partitionMax);
        unitTest.addTest(OrdinalTest::partitionBuilding);
        unitTest.addTest(OrdinalTest::testOnly);
        unitTest.addTest(OrdinalTest::testReplace);
        unitTest.addTest(OrdinalTest::testSwap);
        unitTest.addTest(OrdinalTest::testDropLast);

        unitTest.runTests();
    }

    public static boolean oneSetEquality() {
        return UnitTest.checkAllValues(
            "One set equality",
            List.of(true),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.ONE_SET
                .stream()
                .flatMap(
                    ord1 -> Ordinal.ONE_SET
                        .stream()
                        .map(ord2 -> ord1.equals(ord2))).toList());
    }

    public static boolean twoSetEquality() {
        return UnitTest.checkAllValues(
            "Two set equality",
            List.of(
                true, false, 
                false, true),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.TWO_SET
                .stream()
                .flatMap(
                    ord1 -> Ordinal.TWO_SET
                        .stream()
                        .map(ord2 -> ord1.equals(ord2))).toList());
    }

    public static boolean threeSetEquality() {
        return UnitTest.checkAllValues(
            "Two set equality",
            List.of(
                true, false, false, 
                false, true, false,
                false, false, true),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.THREE_SET
                .stream()
                .flatMap(
                    ord1 -> Ordinal.THREE_SET
                        .stream()
                        .map(ord2 -> ord1.equals(ord2))).toList());
    }

    public static boolean fourSetEquality() {
        return UnitTest.checkAllValues(
            "Two set equality",
            List.of(
                true, false, false, false, 
                false, true, false, false,
                false, false, true, false,
                false, false, false, true),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.FOUR_SET
                .stream()
                .flatMap(
                    ord1 -> Ordinal.FOUR_SET
                        .stream()
                        .map(ord2 -> ord1.equalsOrdinal(ord2))).toList());
    }

    public static boolean fiveSetEquality() {
        return UnitTest.checkAllValues(
            "Two set equality",
            List.of(
                true, false, false, false, false,
                false, true, false, false, false,
                false, false, true, false, false,
                false, false, false, true, false,
                false, false, false, false, true),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.FIVE_SET
                .stream()
                .flatMap(
                    ord1 -> Ordinal.FIVE_SET
                        .stream()
                        .map(ord2 -> ord1.equals(ord2))).toList());
    }

    public static boolean lessThanEqualTest() {
        return UnitTest.checkAllValues(
            "Check less than or equal to",
            List.of(true, true, true, false, false),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(ord -> ord.lessThanEqualOrdinal(Ordinal.TWO_5)).toList());
    }

    public static boolean populateTest() {
        final Function<Ordinal<Five>, Integer> index = Ordinal.populate(1);

        return UnitTest.checkAllValues(
            "Check populate",
            List.of(1, 1, 1, 1, 1),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean partitionTest() {
        final Function<Ordinal<Five>, Integer> index = Ordinal.partition(Ordinal.THREE_5, 0, 1);

        return UnitTest.checkAllValues(
            "Check partition function",
            List.of(0, 0, 0, 0, 1),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean partitionMax() {
        final Function<Ordinal<Five>, Integer> index = Ordinal.partition(Ordinal.FOUR_5, 0, 1);

        return UnitTest.checkAllValues(
            "Check partition function",
            List.of(0, 0, 0, 0, 0),
            a -> b -> a == b, 
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean partitionBuilding() {
        final List<Prod<Ordinal<Five>, Integer>> mapping = List.of(
            Prod.pair(Ordinal.ZERO_5, 0),
            Prod.pair(Ordinal.ONE_5, 1),
            Prod.pair(Ordinal.TWO_5, 2),
            Prod.pair(Ordinal.THREE_5, 3),
            Prod.pair(Ordinal.FOUR_5, 4));

        final Function<Ordinal<Five>, Integer> index = Ordinal.buildPartition(mapping);

        return UnitTest.checkAllValues(
            "Building partitions",
            List.of(0, 1, 2, 3, 4),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean testOnly() {
        final Function<Ordinal<Five>, Boolean> index = Ordinal.only(Ordinal.ONE_5, true, false);

        return UnitTest.checkAllValues(
            "'Kronecker Delta' index function test",
            List.of(false, true, false, false, false),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean testReplace() {
        final Function<Ordinal<Five>, Integer> temp = Ordinal.fiveHomo(1, 2, 3, 4, 5);
        final Function<Ordinal<Five>, Integer> index = Ordinal.replace(temp, Ordinal.THREE_5, -1);

        return UnitTest.checkAllValues(
            "Test index replacement",
            List.of(1, 2, 3, -1, 5),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean testSwap() {
        final Function<Ordinal<Five>, Integer> temp = Ordinal.fiveHomo(1, 2, 3, 4, 5);
        final Function<Ordinal<Five>, Integer> index = Ordinal.swap(temp, Ordinal.THREE_5, Ordinal.ZERO_5);

        return UnitTest.checkAllValues(
            "Test index replacement",
            List.of(4, 2, 3, 1, 5),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.FIVE_SET.stream().map(index).toList());
    }

    public static boolean testDropLast() {
        final Function<Ordinal<Four>, Integer> temp = Ordinal.fourHomo(1, 2, 3, 4);
        final Function<Ordinal<Three>, Integer> index = ord -> Ordinal.dropLast(temp).apply(ord);

        return UnitTest.checkAllValues(
            "Test index replacement",
            List.of(1, 2, 3),
            a -> b -> a == b,
            Object::toString,
            () -> Ordinal.THREE_SET.stream().map(index).toList());
    }
}
