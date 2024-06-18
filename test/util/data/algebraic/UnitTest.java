package test.util.data.algebraic;

import util.data.algebraic.Unit;

public final class UnitTest extends util.testing.UnitTest {
    
    private UnitTest() {
        super("Algebraic Unit Test");
    }

    public static void main(final String[] args) {
        final UnitTest unitTest = new UnitTest();

        unitTest.addTest(UnitTest::destroyUnit);
        unitTest.addTest(UnitTest::unitEquality);
        unitTest.addTest(UnitTest::unitEqualityNull);

        unitTest.runTests();
    }

    private static boolean destroyUnit() {
        return util.testing.UnitTest.expectValue(
            "Destroy unit test", 
            1,
            () -> Unit.unit().destroy(1));
    }

    private static boolean unitEquality() {
        return util.testing.UnitTest.expectValue(
            "Unit equality", 
            Unit.unit(),
            () -> Unit.unit());
    }

    private static boolean unitEqualityNull() {
        return util.testing.UnitTest.preconditionFailure(
            "Unit equality null failure",
            "Failed to throw an exception on a null argument.",
            () -> Unit.unit().equals(null));
    }
}
