package test.util.counting;

import java.util.List;

import util.counting.Ordinal;
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
}
