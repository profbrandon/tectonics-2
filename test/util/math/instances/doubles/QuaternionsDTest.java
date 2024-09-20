package test.util.math.instances.doubles;

import java.util.List;

import util.Functional;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.QuaternionsD;
import util.testing.UnitTest;

public final class QuaternionsDTest extends UnitTest {
    
    private QuaternionsDTest() {
        super("Double-valued Quaternions Test");
    }

    public static void main(final String[] args) {
        final QuaternionsDTest unitTest = new QuaternionsDTest();

        unitTest.addTest(QuaternionsDTest::realMultiplication);
        unitTest.addTest(QuaternionsDTest::negativeOneRoots);
        unitTest.addTest(QuaternionsDTest::normTest);
        unitTest.addTest(QuaternionsDTest::conjugateTest);
        unitTest.addTest(QuaternionsDTest::inverseTest);

        unitTest.runTests();
    }

    public static boolean realMultiplication() {
        return UnitTest.checkValue(
            "Reals closed under multiplication",
            q -> QuaternionsD.INSTANCE.equiv(q, QuaternionsD.INSTANCE.quaternion(6.0, 0.0, 0.0, 0.0)),
            () -> QuaternionsD.INSTANCE.mult(QuaternionsD.INSTANCE.quaternion(2.0, 0.0, 0.0, 0.0), QuaternionsD.INSTANCE.quaternion(3.0, 0.0, 0.0, 0.0)));
    }

    public static boolean negativeOneRoots() {
        final Prod<Prod<Double, Double>, Prod<Double, Double>> NEG_ONE = QuaternionsD.INSTANCE.neg(QuaternionsD.INSTANCE.UNIT_R);

        return UnitTest.checkAllValues(
            "Test roots of negative one",
            List.of(NEG_ONE, NEG_ONE, NEG_ONE),
            (Prod<Prod<Double, Double>, Prod<Double, Double>> q1) -> 
                (Prod<Prod<Double, Double>, Prod<Double, Double>> q2) -> QuaternionsD.INSTANCE.equiv(q1, q2),
            Object::toString,
            () -> List.of(
                QuaternionsD.INSTANCE.mult(QuaternionsD.INSTANCE.UNIT_I, QuaternionsD.INSTANCE.UNIT_I),
                QuaternionsD.INSTANCE.mult(QuaternionsD.INSTANCE.UNIT_J, QuaternionsD.INSTANCE.UNIT_J),
                QuaternionsD.INSTANCE.mult(QuaternionsD.INSTANCE.UNIT_K, QuaternionsD.INSTANCE.UNIT_K)));
    }

    public static boolean normTest() {
        return UnitTest.checkValue(
            "Check the norm",
            d -> DoubleField.INSTANCE.equiv(d, 2.0),
            () -> QuaternionsD.norm(QuaternionsD.INSTANCE.quaternion(1.0, 1.0, 1.0, 1.0)));
    }

    public static boolean conjugateTest() {
        return UnitTest.checkValue(
            "Check conjugation",
            c -> QuaternionsD.INSTANCE.equiv(c, QuaternionsD.INSTANCE.quaternion(2.0, -4.6, 1.2, -3.2)),
            () -> QuaternionsD.INSTANCE.conjugate(QuaternionsD.INSTANCE.quaternion(2.0, 4.6, -1.2, 3.2)));
    }

    public static boolean inverseTest() {
        final Prod<Prod<Double, Double>, Prod<Double, Double>> value = QuaternionsD.INSTANCE.quaternion(-7.1, -2.3, 21.2, -1.0);

        return UnitTest.checkValue(
            "Check inversion", 
            (Prod<Prod<Double, Double>, Prod<Double, Double>> q) -> QuaternionsD.INSTANCE.equiv(q, QuaternionsD.INSTANCE.UNIT_R),
            () -> Functional.let(QuaternionsD.INSTANCE.inv(value), mInverse -> 
                mInverse.match(
                    __  -> QuaternionsD.INSTANCE.zero(),
                    inv -> QuaternionsD.INSTANCE.mult(value, inv))));
    }
}
