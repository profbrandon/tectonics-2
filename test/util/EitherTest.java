package test.util;

import java.util.Optional;

import util.Either;
import util.testing.UnitTest;

public class EitherTest extends UnitTest {
    
    private EitherTest() {
        super("Either Test");
    }

    public static void main(final String[] args) {
        final UnitTest unitTest = new EitherTest();

        unitTest.addTest(EitherTest::leftNullFailure);
        unitTest.addTest(EitherTest::rightNullFailure);
        unitTest.addTest(EitherTest::leftCreationSuccess);
        unitTest.addTest(EitherTest::rightCreationSuccess);
        unitTest.addTest(EitherTest::leftCollapse);
        unitTest.addTest(EitherTest::rightCollapse);
        unitTest.addTest(EitherTest::leftMatchSuccess);
        unitTest.addTest(EitherTest::rightMatchSuccess);
        unitTest.addTest(EitherTest::forgetLeftSuccess);
        unitTest.addTest(EitherTest::forgetRightSuccess);
        unitTest.addTest(EitherTest::forgetLeftFailure);
        unitTest.addTest(EitherTest::forgetRightFailure);
        unitTest.addTest(EitherTest::mapLeftOnLeft);
        unitTest.addTest(EitherTest::mapLeftOnRight);
        unitTest.addTest(EitherTest::mapRightOnRight);
        unitTest.addTest(EitherTest::mapRightOnLeft);
        unitTest.addTest(EitherTest::leftEquality);
        unitTest.addTest(EitherTest::rightEquality);
        unitTest.addTest(EitherTest::leftDisequality);
        unitTest.addTest(EitherTest::rightDisequality);

        unitTest.runTests();
    }

    private static boolean leftNullFailure() {
        return UnitTest.preconditionFailure(
            "Left constructor test", 
            "failed to throw exception on null argument", 
            () -> Either.left(null));
    }

    private static boolean rightNullFailure() {
        return UnitTest.preconditionFailure(
            "Right constructor test", 
            "failed to throw exception on null argument", 
            () -> Either.left(null));
    }

    private static boolean leftCreationSuccess() {
        return UnitTest.expectValue(
            "Creation test left", 
            true, 
            () -> Either.left(0).match(x -> true, y -> false));
    }

    private static boolean rightCreationSuccess() {
        return UnitTest.expectValue(
            "Creation test right", 
            true,
            () -> Either.right(0).match(x -> false, y -> true));
}

    private static boolean leftCollapse() {
        return UnitTest.expectValue(
            "Collapse test left",
            0,
            () -> Either.collapse(Either.left(0)));
    }

    private static boolean rightCollapse() {
        return UnitTest.expectValue(
            "Collapse test right",
            0,
            () -> Either.collapse(Either.right(0)));
    }

    private static boolean leftMatchSuccess() {
        return UnitTest.expectValue(
                "Match test left",
                0,
                () -> Either.left(0).match(x -> x, y -> 1));
    }

    private static boolean rightMatchSuccess() {
        return UnitTest.expectValue(
            "Match test right",
            1, 
            () -> Either.right(1).match(x -> 0, y -> y));
    }

    private static boolean forgetLeftSuccess() {
        return UnitTest.expectValue(
            "Forget left success", 
            Optional.of(1), 
            () -> Either.right(1).forgetLeft());
    }

    private static boolean forgetRightSuccess() {
        return UnitTest.expectValue(
            "Forget right success", 
            Optional.of(0), 
            () -> Either.left(0).forgetRight());
    }

    private static boolean forgetLeftFailure() {
        return UnitTest.expectValue(
            "Forget left failure", 
            Optional.empty(), 
            () -> Either.left(0).forgetLeft());
    }

    private static boolean forgetRightFailure() {
        return UnitTest.expectValue(
            "Forget right failure", 
            Optional.empty(), 
            () -> Either.right(1).forgetRight());
    }

    private static boolean mapLeftOnLeft() {
        return UnitTest.checkValue(
            "Map left on left either", 
            v -> Either.<Boolean, Character>left(true).equalsEither(v),
            () -> Either.mapLeft(Either.<Integer, Character>left(0), x -> true));
    }

    private static boolean mapLeftOnRight() {
        return UnitTest.checkValue(
            "Map left on right either", 
            v -> Either.<Boolean, Character>right('a').equalsEither(v), 
            () -> Either.mapLeft(Either.<Boolean, Character>right('a'), x -> false));
    }

    private static boolean mapRightOnRight() {
        return UnitTest.checkValue(
            "Map left on left either", 
            v -> Either.<Character, Boolean>right(true).equalsEither(v),
            () -> Either.mapRight(Either.<Character, Integer>right(0), x -> true));
    }

    private static boolean mapRightOnLeft() {
        return UnitTest.checkValue(
            "Map left on right either", 
            v -> Either.<Character, Boolean>left('a').equalsEither(v), 
            () -> Either.mapRight(Either.<Character,Boolean>left('a'), x -> false));
    }

    private static boolean leftEquality() {
        return UnitTest.checkValue(
            "Left vs left equality",
            v -> Either.left(0.0).equalsEither(v),
            () -> Either.left(0.0));
    }

    private static boolean rightEquality() {
        return UnitTest.checkValue(
            "Right vs Right equality", 
            v -> Either.right(Optional.of(1)).equalsEither(v),
            () -> Either.right(Optional.of(1)));
    }

    private static boolean leftDisequality() {
        return UnitTest.checkValue(
            "Left vs right disequality", 
            v -> !Either.<Double, Double>left(0.0).equalsEither(v),
            () -> Either.<Double, Double>right(0.0));
    }

    private static boolean rightDisequality() {
        return UnitTest.checkValue(
            "Right vs left disequality", 
            v -> !Either.<Optional<Integer>, Optional<Integer>>right(Optional.of(1)).equalsEither(v),
            () -> Either.<Optional<Integer>, Optional<Integer>>left(Optional.of(1)));
    }
}
