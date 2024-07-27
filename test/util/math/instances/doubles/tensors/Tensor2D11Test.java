package test.util.math.instances.doubles.tensors;

import util.Functional;
import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Sum;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.linear.Linear2D;
import util.math.instances.doubles.tensors.Tensor2D11;
import util.math.instances.doubles.vectors.Vec2D;
import util.testing.UnitTest;

public final class Tensor2D11Test extends UnitTest {
    
    private Tensor2D11Test() {
        super("2 Dimensional Double (1,1)-Tensor Test");
    }

    public static void main(final String[] args) {
        final Tensor2D11Test unitTest = new Tensor2D11Test();

        unitTest.addTest(Tensor2D11Test::linearAndBack);
        unitTest.addTest(Tensor2D11Test::traceTest);

        unitTest.runTests();
    }

    private static boolean linearAndBack() {
        return UnitTest.checkValue(
            "Send linear to tensor and back",
            v -> Vec2D.equalsVector(v, Vec2D.vector(Math.sqrt(3) / 2.0, 0.5)),
            v -> HomTuple.toString(Ordinal.TWO_SET, v),
            () ->
                Functional.let(Linear2D.as11Tensor(Linear2D.getRotation(Math.PI / 6.0)), tensor ->
                    Functional.let(Tensor2D11.asLinearMap(tensor), linear ->
                        linear.apply(Vec2D.vector(1.0, 0)))));
    }

    private static boolean traceTest() {
        final Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> linear = Linear2D.getRotation(Math.PI / 3.0);
        final double trace = Linear2D.trace(linear);
        return UnitTest.checkValue(
            "Check trace (=" + trace + ")", 
            a -> DoubleField.INSTANCE.equiv(a, trace), 
            () ->
                Functional.let(Linear2D.as11Tensor(linear), tensor ->
                    Sum.match(Tensor2D11.INSTANCE.contract(Ordinal.ZERO_1, Ordinal.ZERO_1, tensor),
                        d -> d,
                        __ -> 0.0)));
    }
}
