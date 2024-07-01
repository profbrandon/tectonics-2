package test.util.math.instances.doubles;

import util.counting.OrdinalSet;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.Vec2D;
import util.testing.UnitTest;

public class Vec2DTest extends UnitTest {
    
    private Vec2DTest() {
        super("Vec2D test");
    }

    public static void main(final String[] args) {
        final Vec2DTest unitTest = new Vec2DTest();

        unitTest.addTest(Vec2DTest::equalitySuccess);
        unitTest.addTest(Vec2DTest::equalityFailure);
        unitTest.addTest(Vec2DTest::scaleVector);

        unitTest.runTests();
    }

    private static boolean equalitySuccess() {
        return UnitTest.checkValue(
            "Check for valid equality", 
            v -> Vec2D.equalsVector(Vec2D.UNIT_X, v), 
            () -> new HomTuple<>(OrdinalSet.twoHomo(1.0, 0.0)));
    }

    private static boolean equalityFailure() {
        return UnitTest.checkValue(
            "Check for invalid equality",
            v -> !Vec2D.equalsVector(Vec2D.UNIT_X, v), 
            () -> Vec2D.UNIT_Y);
    }

    private static boolean scaleVector() {
        return UnitTest.checkValue(
            "Scale vector test",
            v -> Vec2D.equalsVector(new HomTuple<>(OrdinalSet.twoHomo(-3.0, 0.0)), v),
            () -> Vec2D.INSTANCE.scale(Vec2D.UNIT_X, -3.0));
    }
}
