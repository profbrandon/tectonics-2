package test.util.data.algebraic;

import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.counting.Cardinals.Four;
import util.counting.Cardinals.Three;
import util.data.algebraic.HomTuple;
import util.testing.UnitTest;

public final class HomTupleTest extends UnitTest {

    private static final HomTuple<Two, Boolean> TUPLE_2 = new HomTuple<>(Ordinal.twoHomo(true, false));
    private static final HomTuple<Three, Character> TUPLE_3 = new HomTuple<>(Ordinal.threeHomo('a', 'b', 'c'));
    private static final HomTuple<Four, Integer> TUPLE_4 = new HomTuple<>(Ordinal.fourHomo(1, 2, 3, 4));
    
    private HomTupleTest() {
        super("Homogeneous Tuple Test");
    }

    public static void main(final String[] args) {
        final HomTupleTest unitTest = new HomTupleTest();

        unitTest.addTest(HomTupleTest::homTupleOnNull);
        unitTest.addTest(HomTupleTest::test1Tuple);
        unitTest.addTest(HomTupleTest::test2TupleFirst);
        unitTest.addTest(HomTupleTest::test2TupleSecond);
        unitTest.addTest(HomTupleTest::test3TupleFirst);
        unitTest.addTest(HomTupleTest::test3TupleSecond);
        unitTest.addTest(HomTupleTest::test3TupleThird);
        unitTest.addTest(HomTupleTest::test4TupleFirst);
        unitTest.addTest(HomTupleTest::test4TupleSecond);
        unitTest.addTest(HomTupleTest::test4TupleThird);
        unitTest.addTest(HomTupleTest::test4TupleFourth);
        unitTest.addTest(HomTupleTest::equalsTupleTrue);
        unitTest.addTest(HomTupleTest::equalsTupleFalse);
        unitTest.addTest(HomTupleTest::correctOutputString);
        
        unitTest.runTests();
    }

    private static boolean homTupleOnNull() {
        return UnitTest.preconditionFailure(
            "Check HomTuple constructor on null",
            "failed to raise exception for null argument",
            () -> new HomTuple<>(null));
    }

    private static boolean test1Tuple() {
        return UnitTest.expectValue(
            "Project from 1-hom-tuple",
            1,
            () -> new HomTuple<>(Ordinal.one(1)).at(Ordinal.ZERO_1));
    }

    private static boolean test2TupleFirst() {
        return UnitTest.expectValue(
            "Project first from 2-hom-tuple",
            true,
            () -> TUPLE_2.at(Ordinal.ZERO_2));
    }

    private static boolean test2TupleSecond() {
        return UnitTest.expectValue(
            "Project first from 2-hom-tuple",
            false,
            () -> TUPLE_2.at(Ordinal.ONE_2));
    }

    private static boolean test3TupleFirst() {
        return UnitTest.expectValue(
            "Project first from 3-hom-tuple",
            'a',
            () -> TUPLE_3.at(Ordinal.ZERO_3));
    }

    private static boolean test3TupleSecond() {
        return UnitTest.expectValue(
            "Project second from 3-hom-tuple",
            'b',
            () -> TUPLE_3.at(Ordinal.ONE_3));
    }

    private static boolean test3TupleThird() {
        return UnitTest.expectValue(
            "Project third from 3-hom-tuple", 
            'c',
            () -> TUPLE_3.at(Ordinal.TWO_3));
    }

    private static boolean test4TupleFirst() {
        return UnitTest.expectValue(
            "Project first from 4-hom-tuple",
            1,
            () -> TUPLE_4.at(Ordinal.ZERO_4));
    }

    private static boolean test4TupleSecond() {
        return UnitTest.expectValue(
            "Project second from 4-hom-tuple",
            2,
            () -> TUPLE_4.at(Ordinal.ONE_4));
    }

    private static boolean test4TupleThird() {
        return UnitTest.expectValue(
            "Project third from 4-hom-tuple", 
            3,
            () -> TUPLE_4.at(Ordinal.TWO_4));
    }

    private static boolean test4TupleFourth() {
        return UnitTest.expectValue(
            "Project fourth from 4-hom-tuple",
            4,
            () -> TUPLE_4.at(Ordinal.THREE_4));
    }

    private static boolean equalsTupleTrue() {
        return UnitTest.checkValue(
            "Check for valid equality",
            v -> v.equalsTuple(Ordinal.FOUR_SET, TUPLE_4),
            () -> TUPLE_4);
    }

    private static boolean equalsTupleFalse() {
        return UnitTest.checkValue(
            "Check for inequality",
            v -> !v.equalsTuple(Ordinal.FOUR_SET, TUPLE_4),
            () -> TUPLE_4.mapAll(x -> x == 2 ? -1 : x));
    }

    private static boolean correctOutputString() {
        return UnitTest.checkValue(
            "Outputs correct string representation",
            v -> v.equals("(1, 2, 3, 4)"),
            () -> HomTuple.toString(Ordinal.FOUR_SET, TUPLE_4));
    }
}
