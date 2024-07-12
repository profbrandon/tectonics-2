package test.util.math.instances.doubles.vectors;

import java.util.List;

import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.vectors.Vec2D;
import util.testing.UnitTest;

public final class Vec2DTest extends UnitTest {
    
    private Vec2DTest() {
        super("Vec2D test");
    }

    public static void main(final String[] args) {
        final Vec2DTest unitTest = new Vec2DTest();

        unitTest.addTest(Vec2DTest::equalitySuccess);
        unitTest.addTest(Vec2DTest::equalityFailure);
        unitTest.addTest(Vec2DTest::scaleVector1);
        unitTest.addTest(Vec2DTest::scaleVector2);
        unitTest.addTest(Vec2DTest::addVector);
        unitTest.addTest(Vec2DTest::negateVector);
        unitTest.addTest(Vec2DTest::dotProductTest);
        unitTest.addTest(Vec2DTest::vectorDecomposition);

        unitTest.runTests();
    }

    private static boolean equalitySuccess() {
        return UnitTest.checkValue(
            "Check for valid equality", 
            v -> Vec2D.equalsVector(Vec2D.UNIT_X, v), 
            () -> new HomTuple<>(Ordinal.twoHomo(1.0, 0.0)));
    }

    private static boolean equalityFailure() {
        return UnitTest.checkValue(
            "Check for invalid equality",
            v -> !Vec2D.equalsVector(Vec2D.UNIT_X, v), 
            () -> Vec2D.UNIT_Y);
    }

    private static boolean scaleVector1() {
        return UnitTest.checkValue(
            "Scale vector test 1",
            v -> Vec2D.equalsVector(Vec2D.vector(-3, 0), v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> Vec2D.INSTANCE.scale(Vec2D.UNIT_X, -3.0));
    }

    private static boolean scaleVector2() {
        return UnitTest.checkValue(
            "Scale vector test 2",
            v -> Vec2D.equalsVector(Vec2D.ZERO, v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> Vec2D.INSTANCE.scale(Vec2D.vector(100, 5), 0.0));
    }

    private static boolean addVector() {
        return UnitTest.checkValue(
            "Add vectors",
            v -> Vec2D.equalsVector(Vec2D.vector(-20, 17), v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> Vec2D.INSTANCE.sum(Vec2D.INSTANCE.scale(Vec2D.UNIT_X, -20.0), Vec2D.INSTANCE.scale(Vec2D.UNIT_Y, 17.0)));
    }

    private static boolean negateVector() {
        return UnitTest.checkValue(
            "Negate vector",
            v -> Vec2D.equalsVector(Vec2D.vector(-13.5, 6.7), v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> Vec2D.INSTANCE.neg(Vec2D.vector(13.5, -6.7)));
    }

    private static boolean dotProductTest() {
        return UnitTest.checkValue(
            "Dot product test",
            d -> DoubleField.INSTANCE.equiv(d, -25.0),
            () -> Vec2D.INSTANCE.dot(Vec2D.vector(3, 4), Vec2D.vector(-3, -4)));
    }

    private static boolean vectorDecomposition() {
        return UnitTest.checkValue(
            "Test vector decomposition",
            ds -> {
                final List<Double> doubles = ds.stream().map(pair -> pair.first()).toList();
                return 
                    DoubleField.INSTANCE.equiv(-7.4, doubles.get(0)) &&
                    DoubleField.INSTANCE.equiv(9.102, doubles.get(1));
            },
            () -> Vec2D.INSTANCE.decompose(Vec2D.vector(-7.4, 9.102)));
    }
}
