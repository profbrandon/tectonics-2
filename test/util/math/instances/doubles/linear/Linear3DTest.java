package test.util.math.instances.doubles.linear;

import java.util.List;

import util.counting.Ordinal;
import util.counting.Cardinals.Three;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.Vec3D;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.linear.Linear3D;
import util.math.instances.doubles.linear.LinearD;
import util.testing.UnitTest;

public final class Linear3DTest extends UnitTest {

    private static final Exp<HomTuple<Three, Double>, HomTuple<Three, Double>> LINEAR_1 =
        Linear3D.asLinearMap(3.0, -1.2, 7.79, -0.2, 2.4, 1.0, -0.01, -10.67, 5.31);
    
    private Linear3DTest() {
        super("Linear 3D Double Endomorphism Test");
    }

    public static void main(final String[] args) {
        final Linear3DTest unitTest = new Linear3DTest();

        unitTest.addTest(Linear3DTest::decomposeTest);
        unitTest.addTest(Linear3DTest::decomposeIdentity);
        unitTest.addTest(Linear3DTest::decomposeZero);
        unitTest.addTest(Linear3DTest::testEquiv);
        unitTest.addTest(Linear3DTest::testRotate);
        unitTest.addTest(Linear3DTest::rotationComposition1);
        unitTest.addTest(Linear3DTest::rotationComposition2);
        unitTest.addTest(Linear3DTest::testMult1);
        unitTest.addTest(Linear3DTest::testMult2);
        unitTest.addTest(Linear3DTest::scaling);

        unitTest.runTests();
    }

    private static boolean decomposeTest() {
        return UnitTest.checkAllValues(
            "Linear Map decomposition", 
            List.of(3.0, -1.2, 7.79, -0.2, 2.4, 1.0, -0.01, -10.67, 5.31),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear3D.INSTANCE.decompose(LINEAR_1).stream().map(pair -> pair.first()).toList());
    }

    private static boolean decomposeIdentity() {
        return UnitTest.checkAllValues(
            "Idenitity decomposition", 
            List.of(
                1.0, 0.0, 0.0,
                0.0, 1.0, 0.0,
                0.0, 0.0, 1.0),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear3D.INSTANCE.decompose(Linear3D.IDENTITY).stream().map(pair -> pair.first()).toList());
    }

    private static boolean decomposeZero() {
        return UnitTest.checkAllValues(
            "Zero decomposition", 
            List.of(
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0),
            d1 -> d2 -> DoubleField.INSTANCE.equiv(d1, d2),
            Object::toString,
            () -> Linear3D.INSTANCE.decompose(Linear3D.ZERO).stream().map(pair -> pair.first()).toList());
    }

    private static boolean testEquiv() {
        return UnitTest.checkValue(
            "Linear map equivalence", 
            m -> Linear3D.INSTANCE.equiv(m, LINEAR_1),
            () -> Linear3D.INSTANCE.mult(LINEAR_1, Linear3D.IDENTITY));
    }

    private static boolean testRotate() {
        return UnitTest.checkValue(
            "Rotate vectors",
            v -> Vec3D.equalsVector(v, Vec3D.UNIT_Y),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Linear3D.INSTANCE.transform(Linear3D.getRotation(Vec3D.vector(1, 1, 1), 2.0 * Math.PI / 3.0), Vec3D.UNIT_X));
    }

    private static boolean rotationComposition1() {
        return UnitTest.checkValue(
            "Test rotation composition",
            v -> Vec3D.equalsVector(v, Vec3D.UNIT_Z),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () ->
                Linear3D.INSTANCE.transform(
                    LinearD.compose(
                        Linear3D.getRotation(Vec3D.UNIT_Z, Math.PI / 2.0),
                        Linear3D.getRotation(Vec3D.UNIT_X, Math.PI / 2.0)),
                    Vec3D.UNIT_X));
    }

    private static boolean rotationComposition2() {
        return UnitTest.checkValue(
            "Test rotation composition",
            v -> Vec3D.equalsVector(v, Vec3D.UNIT_Y),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () ->
                Linear3D.INSTANCE.transform(
                    LinearD.compose(
                        Linear3D.getRotation(Vec3D.UNIT_X, Math.PI / 2.0),
                        Linear3D.getRotation(Vec3D.UNIT_Z, Math.PI / 2.0)),
                    Vec3D.UNIT_X));
    }

    private static boolean testMult1() {
        return UnitTest.checkValue(
            "Test linear composition (matrix multiplication) 1",
            v -> Vec3D.equalsVector(v, Vec3D.INSTANCE.neg(Vec3D.UNIT_X)),
            () -> Linear3D.INSTANCE.transform(
                Linear3D.INSTANCE.mult(
                    Linear3D.getRotation(Vec3D.UNIT_Z, Math.PI / 4.0),
                    Linear3D.getRotation(Vec3D.UNIT_Z, 3.0 * Math.PI / 4.0)),
                Vec3D.UNIT_X));
    }

    private static boolean testMult2() {
        return UnitTest.checkValue(
            "Test linear composition 2",
            m -> Linear3D.INSTANCE.equiv(m, Linear3D.IDENTITY),
            () -> Linear3D.INSTANCE.mult(
                Linear3D.EXCHANGE,
                Linear3D.EXCHANGE));
    }

    private static boolean scaling() {
        return UnitTest.checkValue(
            "Test vector scaling (and rotation)",
            v -> Vec3D.equalsVector(v, Vec3D.vector(2.0 * Math.sqrt(3.0), 2.0, 0.0)),
            () -> Linear3D.INSTANCE.transform(
                Linear3D.INSTANCE.mult(
                    Linear3D.getScale(4.0),
                    Linear3D.getRotation(Vec3D.UNIT_Z, Math.PI / 6.0)),
                Vec3D.UNIT_X));
    }
}
