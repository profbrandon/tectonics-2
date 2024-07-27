package test.util.data.algebraic;

import java.util.function.Function;

import util.data.algebraic.Exp;
import util.data.algebraic.Prod;
import util.testing.UnitTest;

public final class ExpTest extends UnitTest {

    private ExpTest() {
        super("Algebraic Exponential Test");
    }

    public static void main(final String[] args) {
        final ExpTest unitTest = new ExpTest();

        unitTest.addTest(ExpTest::equalsExpIdentity);
        unitTest.addTest(ExpTest::equalsExpConstant);
        unitTest.addTest(ExpTest::equalsExpSelf);
        unitTest.addTest(ExpTest::application);
        unitTest.addTest(ExpTest::asExponentialOnNull);
        unitTest.addTest(ExpTest::afterOnNull);
        unitTest.addTest(ExpTest::after);
        unitTest.addTest(ExpTest::constantOnNull);
        unitTest.addTest(ExpTest::identity);
        unitTest.addTest(ExpTest::evaluation);
        unitTest.addTest(ExpTest::evaluationNull1);
        unitTest.addTest(ExpTest::evaluationNull2);
        unitTest.addTest(ExpTest::evaulateCurryConstant);
        unitTest.addTest(ExpTest::curry);
        unitTest.addTest(ExpTest::uncurry);

        unitTest.runTests();
    }

    private static boolean equalsExpIdentity() {
        return UnitTest.checkValue(
            "Identity equals test",
            v -> Exp.<Integer>identity().equalsExp(v),
            () -> Exp.<Integer>identity());
    }

    private static boolean equalsExpConstant() {
        return UnitTest.checkValue(
            "Exponential constant equality test",
            v -> Exp.<Integer, Boolean>constant(false).equalsExp(v),
            () -> Exp.<Integer, Boolean>constant(false));
    }

    private static boolean equalsExpSelf() {
        final Function<String, Integer> fun = String::length;

        // It's interesting that this fails if String::lenght is passed separately twice

        return UnitTest.checkValue(
            "Exponential reflexivity test",
            v -> Exp.asExponential(fun).equalsExp(v),
            () -> Exp.asExponential(fun));
    }

    private static boolean application() {
        return UnitTest.expectValue(
            "Application test",
            9,
            () -> Exp.<Integer, Integer>asExponential(a -> a + 5).apply(4));
    }

    private static boolean asExponentialOnNull() {
        return UnitTest.preconditionFailure(
            "Check Exp.asExponential() throws on null",
            "Failed to throw an exception on a null argument", 
            () -> Exp.asExponential(null));
    }

    private static boolean afterOnNull() {
        return UnitTest.preconditionFailure(
            "Check Exp.after() throws on null",
            "Failed to throw an exception on a null argument", 
            () -> Exp.<Integer, Integer>asExponential(a -> a + 1).after(null));
    }

    private static boolean after() {
        return UnitTest.expectValue(
            "Check composition", 
            2, 
            () -> {
                final Exp<Integer, Integer> exp = Exp.asExponential(a -> a + 1);
                return exp.after(exp).apply(0);
            });
    }

    private static boolean constantOnNull() {
        return UnitTest.preconditionFailure(
            "Check Exp.constant() throws on null", 
            "Failed to throw an exception on a null argument",
            () -> Exp.constant(null));
    }

    private static boolean identity() {
        return UnitTest.expectValue(
            "Identity acts as identity",
            0,
            () -> Exp.<Integer>identity().apply(0));
    }

    private static boolean evaluation() {
        return UnitTest.expectValue(
            "Evaluation test",
            9,
            () -> Exp.eval(Exp.<Integer, Integer>asExponential(a -> a + 5), 4));
    }

    private static boolean evaluationNull1() {
        return UnitTest.preconditionFailure(
            "Evaulation failure on null arg1", 
            "Failed to throw an exception on a null first argument", 
            () -> Exp.eval(null, 10));
    }

    private static boolean evaluationNull2() {
        return UnitTest.preconditionFailure(
            "Evaulation failure on null arg1", 
            "Failed to throw an exception on a null first argument", 
            () -> Exp.eval(Exp.constant(10), null));
    }

    private static boolean evaulateCurryConstant() {
        return UnitTest.checkValue(
            "Exponential that returns a constant exponential", 
            v -> Exp.<String, Boolean>constant(false).equalsExp(v.apply(0)),
            () -> Exp.<Integer,Exp<String,Boolean>>asExponential(a -> Exp.<String, Boolean>constant(false)));
    }

    private static boolean curry() {
        return UnitTest.expectValue(
            "Test curry",
            "Hello, World!",
            () -> Exp.curry(
                (Prod<String, String> pair) -> 
                    pair.first() + pair.second())
                .apply("Hello, ")
                .apply("World!"));
    }

    private static boolean uncurry() {
        return UnitTest.expectValue(
            "Test uncurry",
            "Hello, World!",
            () -> Exp.uncurry(
                (String a) -> 
                    Exp.asExponential(
                        (String b) -> a + b))
                .apply(Prod.pair("Hello, ", "World!")));
    }
}
