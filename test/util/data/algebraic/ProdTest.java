package test.util.data.algebraic;

import java.util.List;
import java.util.Optional;

import util.data.algebraic.Sum;
import util.data.algebraic.Prod;
import util.testing.UnitTest;

public final class ProdTest extends UnitTest {
    
    private ProdTest() {
        super("Prod Test");
    }

    public static void main(final String[] args) {
        final ProdTest unitTest = new ProdTest();

        unitTest.addTest(ProdTest::firstNull);
        unitTest.addTest(ProdTest::secondNull);
        unitTest.addTest(ProdTest::firstProjection);
        unitTest.addTest(ProdTest::secondProjection);
        unitTest.addTest(ProdTest::destroy);
        unitTest.addTest(ProdTest::mapFirst);
        unitTest.addTest(ProdTest::mapSecond);
        unitTest.addTest(ProdTest::equality);
        unitTest.addTest(ProdTest::disequalityFirst);
        unitTest.addTest(ProdTest::disequalitySecond);
        unitTest.addTest(ProdTest::mapBoth);

        unitTest.runTests();
    }

    private static boolean firstNull() {
        return UnitTest.preconditionFailure(
            "Pair first null failure", 
            "failed to raise exception on null first argument",
            () -> Prod.pair(null, 1));
    }

    private static boolean secondNull() {
        return UnitTest.preconditionFailure(
            "Pair second null failure", 
            "failed to raise exception on null second argument", 
            () -> Prod.pair(false, null));
    }

    private static boolean firstProjection() {
        return UnitTest.expectValue(
            "First projection test",
            'a',
            () -> Prod.pair('a', false).first());
    }

    private static boolean secondProjection() {
        return UnitTest.checkValue(
            "Second projection test", 
            v -> Sum.left(0).equalsSum(v),
            () -> Prod.pair("xyz", Sum.left(0)).second());
    }

    private static boolean destroy() {
        return UnitTest.expectValue(
            "Destruction test", 
            0,
            () -> Prod.pair(-5, 5).destroy(a -> b -> a + b));
    }

    private static boolean mapFirst() {
        return UnitTest.checkValue(
            "Map first element",
            v -> Prod.pair(0, false).equalsProd(v),
            () -> Prod.mapFirst(Prod.pair('a', false), x -> x == 'a' ? 0 : 1));
    }

    private static boolean mapSecond() {
        return UnitTest.checkValue(
            "Map second element",
            v -> Prod.pair('a', false).equalsProd(v),
            () -> Prod.mapSecond(Prod.pair('a', 0), x -> x != 0));
    }

    private static boolean equality() {
        return UnitTest.checkValue(
            "Equality test",
            v -> Prod.pair(0, false).equalsProd(v),
            () -> Prod.pair(0, false));
    }

    private static boolean disequalityFirst() {
        return UnitTest.checkValue(
            "Disequality test in first argument",
            v -> !Prod.pair("x", true).equalsProd(v),
            () -> Prod.pair("y", true));
    }

    private static boolean disequalitySecond() {
        return UnitTest.checkValue(
            "Disequality test in second argument", 
            v -> !Prod.pair("x", Optional.of(3)).equalsProd(v),
            () -> Prod.pair("x", Optional.of(4)));
    }

    private static boolean mapBoth() {
        return UnitTest.checkValue(
            "Map both product elements",
            v -> Prod.pair(6,6).equalsProd(v), 
            () -> Prod.map(Prod.pair(List.of(1,2,3,4,5,6), "123456"), List::size, String::length));
    }
}
