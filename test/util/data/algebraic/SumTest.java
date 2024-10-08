package test.util.data.algebraic;

import java.util.List;
import java.util.Optional;

import util.data.algebraic.Sum;
import util.testing.UnitTest;

public final class SumTest extends UnitTest {
    
    private SumTest() {
        super("Sum Type Test");
    }

    public static void main(final String[] args) {
        final UnitTest unitTest = new SumTest();

        unitTest.addTest(SumTest::leftNullFailure);
        unitTest.addTest(SumTest::rightNullFailure);
        unitTest.addTest(SumTest::leftCreationSuccess);
        unitTest.addTest(SumTest::rightCreationSuccess);
        unitTest.addTest(SumTest::leftCollapse);
        unitTest.addTest(SumTest::rightCollapse);
        unitTest.addTest(SumTest::leftMatchSuccess);
        unitTest.addTest(SumTest::rightMatchSuccess);
        unitTest.addTest(SumTest::forgetLeftSuccess);
        unitTest.addTest(SumTest::forgetRightSuccess);
        unitTest.addTest(SumTest::forgetLeftFailure);
        unitTest.addTest(SumTest::forgetRightFailure);
        unitTest.addTest(SumTest::mapLeftOnLeft);
        unitTest.addTest(SumTest::mapLeftOnRight);
        unitTest.addTest(SumTest::mapRightOnRight);
        unitTest.addTest(SumTest::mapRightOnLeft);
        unitTest.addTest(SumTest::leftEquality);
        unitTest.addTest(SumTest::rightEquality);
        unitTest.addTest(SumTest::leftDisequality);
        unitTest.addTest(SumTest::rightDisequality);
        unitTest.addTest(SumTest::mapOnLeft);
        unitTest.addTest(SumTest::mapOnRight);

        unitTest.runTests();
    }

    private static boolean leftNullFailure() {
        return UnitTest.preconditionFailure(
            "Left constructor test", 
            "failed to throw exception on null argument", 
            () -> Sum.left(null));
    }

    private static boolean rightNullFailure() {
        return UnitTest.preconditionFailure(
            "Right constructor test", 
            "failed to throw exception on null argument", 
            () -> Sum.left(null));
    }

    private static boolean leftCreationSuccess() {
        return UnitTest.expectValue(
            "Creation test left", 
            true, 
            () -> Sum.left(0).match(x -> true, y -> false));
    }

    private static boolean rightCreationSuccess() {
        return UnitTest.expectValue(
            "Creation test right", 
            true,
            () -> Sum.right(0).match(x -> false, y -> true));
}

    private static boolean leftCollapse() {
        return UnitTest.expectValue(
            "Collapse test left",
            0,
            () -> Sum.collapse(Sum.left(0)));
    }

    private static boolean rightCollapse() {
        return UnitTest.expectValue(
            "Collapse test right",
            0,
            () -> Sum.collapse(Sum.right(0)));
    }

    private static boolean leftMatchSuccess() {
        return UnitTest.expectValue(
                "Match test left",
                0,
                () -> Sum.left(0).match(x -> x, y -> 1));
    }

    private static boolean rightMatchSuccess() {
        return UnitTest.expectValue(
            "Match test right",
            1, 
            () -> Sum.right(1).match(x -> 0, y -> y));
    }

    private static boolean forgetLeftSuccess() {
        return UnitTest.expectValue(
            "Forget left success", 
            Optional.of(1), 
            () -> Sum.right(1).forgetLeft());
    }

    private static boolean forgetRightSuccess() {
        return UnitTest.expectValue(
            "Forget right success", 
            Optional.of(0), 
            () -> Sum.left(0).forgetRight());
    }

    private static boolean forgetLeftFailure() {
        return UnitTest.expectValue(
            "Forget left failure", 
            Optional.empty(), 
            () -> Sum.left(0).forgetLeft());
    }

    private static boolean forgetRightFailure() {
        return UnitTest.expectValue(
            "Forget right failure", 
            Optional.empty(), 
            () -> Sum.right(1).forgetRight());
    }

    private static boolean mapLeftOnLeft() {
        return UnitTest.checkValue(
            "Map left on left either", 
            v -> Sum.<Boolean, Character>left(true).equalsSum(v),
            () -> Sum.mapLeft(Sum.<Integer, Character>left(0), x -> true));
    }

    private static boolean mapLeftOnRight() {
        return UnitTest.checkValue(
            "Map left on right either", 
            v -> Sum.<Boolean, Character>right('a').equalsSum(v), 
            () -> Sum.mapLeft(Sum.<Boolean, Character>right('a'), x -> false));
    }

    private static boolean mapRightOnRight() {
        return UnitTest.checkValue(
            "Map left on left either", 
            v -> Sum.<Character, Boolean>right(true).equalsSum(v),
            () -> Sum.mapRight(Sum.<Character, Integer>right(0), x -> true));
    }

    private static boolean mapRightOnLeft() {
        return UnitTest.checkValue(
            "Map left on right either", 
            v -> Sum.<Character, Boolean>left('a').equalsSum(v), 
            () -> Sum.mapRight(Sum.<Character,Boolean>left('a'), x -> false));
    }

    private static boolean leftEquality() {
        return UnitTest.checkValue(
            "Left vs left equality",
            v -> Sum.left(0.0).equalsSum(v),
            () -> Sum.left(0.0));
    }

    private static boolean rightEquality() {
        return UnitTest.checkValue(
            "Right vs Right equality", 
            v -> Sum.right(Optional.of(1)).equalsSum(v),
            () -> Sum.right(Optional.of(1)));
    }

    private static boolean leftDisequality() {
        return UnitTest.checkValue(
            "Left vs right disequality", 
            v -> !Sum.<Double, Double>left(0.0).equalsSum(v),
            () -> Sum.<Double, Double>right(0.0));
    }

    private static boolean rightDisequality() {
        return UnitTest.checkValue(
            "Right vs left disequality", 
            v -> !Sum.<Optional<Integer>, Optional<Integer>>right(Optional.of(1)).equalsSum(v),
            () -> Sum.<Optional<Integer>, Optional<Integer>>left(Optional.of(1)));
    }

    private static boolean mapOnLeft() {
        return UnitTest.checkValue(
            "Map on left either", 
            v -> Sum.<Integer, Integer>left(6).equalsSum(v),
            () -> Sum.map(Sum.<List<Integer>, String>left(List.of(1,2,3,4,5,6)), List::size, String::length));
    }

    private static boolean mapOnRight() {
        return UnitTest.checkValue(
            "Map on right either", 
            v -> Sum.<Integer, Integer>right(6).equalsSum(v),
            () -> Sum.map(Sum.<List<Integer>, String>right("123456"), List::size, String::length));
    }
}
