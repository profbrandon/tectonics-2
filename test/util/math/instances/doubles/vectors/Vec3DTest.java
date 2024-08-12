package test.util.math.instances.doubles.vectors;

import java.util.List;

import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.vectors.Vec3D;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.QuaternionsD;
import util.testing.UnitTest;

public final class Vec3DTest extends UnitTest {
    
    private Vec3DTest() {
        super("3-Dimensional Double-valued Vector Test");
    }

    public static void main(final String[] args) {
        final Vec3DTest unitTest = new Vec3DTest();

        unitTest.addTest(Vec3DTest::equalitySuccess);
        unitTest.addTest(Vec3DTest::equalityFailure);
        unitTest.addTest(Vec3DTest::scaleVector1);
        unitTest.addTest(Vec3DTest::scaleVector2);
        unitTest.addTest(Vec3DTest::addVector);
        unitTest.addTest(Vec3DTest::negateVector);
        unitTest.addTest(Vec3DTest::dotProductTest);
        unitTest.addTest(Vec3DTest::vectorDecomposition);
        unitTest.addTest(Vec3DTest::vectorBasis);
        unitTest.addTest(Vec3DTest::rotateWithQuaternions1);
        unitTest.addTest(Vec3DTest::rotateWithQuaternions2);

        unitTest.runTests();
    }

    private static boolean equalitySuccess() {
        return UnitTest.checkValue(
            "Check for valid equality", 
            v -> Vec3D.equalsVector(Vec3D.UNIT_X, v), 
            () -> HomTuple.tuple(1.0, 0.0, 0.0));
    }

    private static boolean equalityFailure() {
        return UnitTest.checkValue(
            "Check for invalid equality",
            v -> !Vec3D.equalsVector(Vec3D.UNIT_X, v), 
            () -> Vec3D.UNIT_Y);
    }

    private static boolean scaleVector1() {
        return UnitTest.checkValue(
            "Scale vector test 1",
            v -> Vec3D.equalsVector(Vec3D.vector(-3, 0, 0), v),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Vec3D.INSTANCE.scale(Vec3D.UNIT_X, -3.0));
    }

    private static boolean scaleVector2() {
        return UnitTest.checkValue(
            "Scale vector test 2",
            v -> Vec3D.equalsVector(Vec3D.ZERO, v),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Vec3D.INSTANCE.scale(Vec3D.vector(100, 5, -3), 0.0));
    }

    private static boolean addVector() {
        return UnitTest.checkValue(
            "Add vectors",
            v -> Vec3D.equalsVector(Vec3D.vector(-20, 17, -1.5), v),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Vec3D.INSTANCE.sumAll(List.of(
                Vec3D.INSTANCE.scale(Vec3D.UNIT_X, -20.0), 
                Vec3D.INSTANCE.scale(Vec3D.UNIT_Y, 17.0),
                Vec3D.INSTANCE.scale(Vec3D.UNIT_Z, -1.5))));
    }

    private static boolean negateVector() {
        return UnitTest.checkValue(
            "Negate vector",
            v -> Vec3D.equalsVector(Vec3D.vector(-13.5, 6.7, -23.3), v),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Vec3D.INSTANCE.neg(Vec3D.vector(13.5, -6.7, 23.3)));
    }

    private static boolean dotProductTest() {
        return UnitTest.checkValue(
            "Dot product test",
            d -> DoubleField.INSTANCE.equiv(d, -25.0 - 49),
            () -> Vec3D.INSTANCE.dot(Vec3D.vector(3, 4, 7), Vec3D.vector(-3, -4, -7)));
    }

    private static boolean vectorDecomposition() {
        return UnitTest.checkAllValues(
            "Test vector decomposition",
            List.of(-7.4, 9.102, 1.1),
            x -> y -> DoubleField.INSTANCE.equiv(x, y),
            Object::toString,
            () -> Vec3D.INSTANCE.decompose(Vec3D.vector(-7.4, 9.102, 1.1)).stream().map(Prod::first).toList());
    }

    private static boolean vectorBasis() {
        return UnitTest.checkAllValues(
            "Enumerate basis",
            List.of(Vec3D.UNIT_X, Vec3D.UNIT_Y, Vec3D.UNIT_Z),
            v -> w -> Vec3D.equalsVector(v, w),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> Vec3D.INSTANCE.basis());
    }

    private static boolean rotateWithQuaternions1() {
        return UnitTest.checkValue(
            "Rotate with Quaternion 1",
            v -> Vec3D.INSTANCE.equiv(v, Vec3D.UNIT_Y),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> QuaternionsD.rotation(Vec3D.UNIT_Z, Math.PI / 2.0).apply(Vec3D.UNIT_X));
    }

    private static boolean rotateWithQuaternions2() {
        return UnitTest.checkValue(
            "Rotate with Quaternion 2",
            v -> Vec3D.INSTANCE.equiv(
                v, 
                Vec3D.vector(
                    Math.sqrt(2) / 2.0, 
                    0, 
                    -Math.sqrt(2) / 2.0)),
            v -> HomTuple.toString(Ordinal.THREE_SET, v),
            () -> QuaternionsD.rotation(Vec3D.UNIT_Y, Math.PI / 4.0).apply(Vec3D.UNIT_X));
    }
}
