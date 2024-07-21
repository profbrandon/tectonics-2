package test.util.math.instances.doubles.covectors;

import java.util.List;

import util.Functional;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.covectors.CoVec2D;
import util.math.instances.doubles.vectors.Vec2D;
import util.testing.UnitTest;

public final class CoVec2DTest extends UnitTest {
    
    private CoVec2DTest() {
        super("2 Dimensional Double Covector Test");
    }

    public static void main(final String[] args) {
        final CoVec2DTest unitTest = new CoVec2DTest();

        unitTest.addTest(CoVec2DTest::dualIso);
        unitTest.addTest(CoVec2DTest::vectorToDualDual);
        unitTest.addTest(CoVec2DTest::dualDualIso);

        unitTest.runTests();
    }

    private static boolean dualIso() {
        return UnitTest.checkValue(
            "Assert the validity of the V = V* isomorphism",
            v -> Vec2D.INSTANCE.equiv(Vec2D.vector(-4, 1), v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> 
                CoVec2D.INSTANCE.dualAsVector(
                    CoVec2D.INSTANCE.vectorAsDual(Vec2D.vector(-4, 1))));
    }

    private static boolean vectorToDualDual() {
        return UnitTest.checkAllValues(
            "Check that the components are preserved under V -> V**",
            List.of(2.0, 3.0),
            a -> b -> DoubleField.INSTANCE.equiv(a, b),
            Object::toString,
            () -> 
                Functional.let(CoVec2D.INSTANCE.asDualDual(Vec2D.vector(2.0, 3.0)), dd -> 
                    Vec2D.INSTANCE.basis()
                        .stream()
                        .map(b -> 
                            dd.apply(CoVec2D.INSTANCE.vectorAsDual(b)))
                        .toList()));
    }

    private static boolean dualDualIso() {
        return UnitTest.checkValue(
            "Assert the validity of the V = V** isomorphism",
            v -> Vec2D.INSTANCE.equiv(Vec2D.vector(2, 3), v),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () -> 
                CoVec2D.INSTANCE.dualDualAsVector(
                    CoVec2D.INSTANCE.asDualDual(Vec2D.vector(2, 3))));
    }
}
