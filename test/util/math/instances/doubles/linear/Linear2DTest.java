package test.util.math.instances.doubles.linear;

import java.util.List;

import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.linear.Linear2D;
import util.math.instances.doubles.vectors.Vec2D;
import util.testing.UnitTest;

public final class Linear2DTest extends UnitTest {

    private static final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> LINEAR_1 =
        Linear2D.asLinearMap(3.0, -1.2, 7.79, -0.2);
    
    private Linear2DTest() {
        super("Linear 2D Double Endomorphism Test");
    }

    public static void main(final String[] args) {
        final Linear2DTest unitTest = new Linear2DTest();

        unitTest.addTest(Linear2DTest::decomposeTest);
        unitTest.addTest(Linear2DTest::decomposeIdentity);
        unitTest.addTest(Linear2DTest::decomposeZero);
        unitTest.addTest(Linear2DTest::testEquiv);
        unitTest.addTest(Linear2DTest::testRotate);
        unitTest.addTest(Linear2DTest::testMult1);
        unitTest.addTest(Linear2DTest::testMult2);
        unitTest.addTest(Linear2DTest::scaling);

        unitTest.runTests();
    }

    private static boolean decomposeTest() {
        return UnitTest.checkAllValues(
            "Linear Map decomposition", 
            List.of(3.0, -1.2, 7.79, -0.2),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear2D.INSTANCE.decompose(LINEAR_1).stream().map(pair -> pair.first()).toList());
    }

    private static boolean decomposeIdentity() {
        return UnitTest.checkAllValues(
            "Idenitity decomposition", 
            List.of(
                1.0, 0.0,
                0.0, 1.0),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear2D.INSTANCE.decompose(Linear2D.IDENTITY).stream().map(pair -> pair.first()).toList());
    }

    private static boolean decomposeZero() {
        return UnitTest.checkAllValues(
            "Zero decomposition", 
            List.of(
                0.0, 0.0,
                0.0, 0.0),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear2D.INSTANCE.decompose(Linear2D.ZERO).stream().map(pair -> pair.first()).toList());
    }

    private static boolean testEquiv() {
        return UnitTest.checkValue(
            "Linear map equivalence", 
            m -> Linear2D.INSTANCE.equiv(m, LINEAR_1),
            () -> Linear2D.INSTANCE.mult(LINEAR_1, Linear2D.IDENTITY));
    }

    private static boolean testRotate() {
        return UnitTest.checkValue(
            "Rotate vectors",
            v -> Vec2D.equalsVector(v, Vec2D.UNIT_Y),
            () -> Linear2D.INSTANCE.transform(Linear2D.getRotation(Math.PI / 2.0), Vec2D.UNIT_X));
    }

    private static boolean testMult1() {
        return UnitTest.checkValue(
            "Test linear composition (matrix multiplication) 1",
            v -> Vec2D.equalsVector(v, Vec2D.INSTANCE.neg(Vec2D.UNIT_X)),
            () -> Linear2D.INSTANCE.transform(
                Linear2D.INSTANCE.mult(
                    Linear2D.getRotation(Math.PI / 4.0),
                    Linear2D.getRotation(3.0 * Math.PI / 4.0)),
                Vec2D.UNIT_X));
    }

    private static boolean testMult2() {
        return UnitTest.checkValue(
            "Test linear composition 2",
            m -> Linear2D.INSTANCE.equiv(m, Linear2D.IDENTITY),
            () -> Linear2D.INSTANCE.mult(
                Linear2D.EXCHANGE,
                Linear2D.EXCHANGE));
    }

    private static boolean scaling() {
        return UnitTest.checkValue(
            "Test vector scaling (and rotation)",
            v -> Vec2D.equalsVector(v, Vec2D.vector(2.0 * Math.sqrt(3.0), 2.0)),
            () -> Linear2D.INSTANCE.transform(
                Linear2D.INSTANCE.mult(
                    Linear2D.getScale(4.0),
                    Linear2D.getRotation(Math.PI / 6.0)),
                Vec2D.UNIT_X));
    }
}
