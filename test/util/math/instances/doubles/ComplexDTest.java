package test.util.math.instances.doubles;

import util.Functional;
import util.data.algebraic.Prod;
import util.math.instances.doubles.ComplexD;
import util.math.instances.doubles.DoubleField;
import util.testing.UnitTest;

public final class ComplexDTest extends UnitTest {
    
    private ComplexDTest() {
        super("Complex Doubles Test");
    }

    public static void main(final String[] args) {
        final ComplexDTest unitTest = new ComplexDTest();

        unitTest.addTest(ComplexDTest::normTest);
        unitTest.addTest(ComplexDTest::conjugateTest);
        unitTest.addTest(ComplexDTest::inverseTest);

        unitTest.runTests();
    }

    public static boolean normTest() {
        return UnitTest.checkValue(
            "Check the norm",
            d -> DoubleField.INSTANCE.equiv(d, Math.sqrt(2)),
            () -> ComplexD.norm(ComplexD.INSTANCE.complex(1.0, 1.0)));
    }

    public static boolean conjugateTest() {
        return UnitTest.checkValue(
            "Check conjugation",
            c -> ComplexD.INSTANCE.equiv(c, ComplexD.INSTANCE.complex(2.0, -4.6)),
            () -> ComplexD.INSTANCE.conjugate(ComplexD.INSTANCE.complex(2.0, 4.6)));
    }

    public static boolean inverseTest() {
        final Prod<Double, Double> value = ComplexD.INSTANCE.complex(-7.1, -2.3);

        return UnitTest.checkValue(
            "Check inversion", 
            (Prod<Double, Double> c) -> ComplexD.INSTANCE.equiv(c, ComplexD.INSTANCE.unit()),
            () -> Functional.let(ComplexD.INSTANCE.inv(value), mInverse -> 
                mInverse.match(
                    __  -> ComplexD.INSTANCE.zero(),
                    inv -> ComplexD.INSTANCE.mult(value, inv))));
    }
}
