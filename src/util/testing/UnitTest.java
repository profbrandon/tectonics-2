package util.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UnitTest {
    private final String testName;
    private final Collection<Supplier<Boolean>> tests = new ArrayList<>();

    public UnitTest(final String testName) {
        this.testName = testName;
    }

    public void addTest(final Supplier<Boolean> test) {
        tests.add(test);
    }

    public void runTests() {
        System.out.println("Beginning test '" + this.testName + "':");

        int failedTests = 0;

        for (final Supplier<Boolean> test : this.tests) {
            try {
                if (!test.get()) ++failedTests;
            } catch(final Exception exception) {
                ++failedTests;
                System.out.println("Unexpected exception occurred in subtest: " + exception);
            }
        }

        System.out.print("Test '" + this.testName + "' concluded: ");

        if (failedTests == 0) {
            System.out.print("all tests passed, ");
        }

        System.out.println(failedTests + "/" + tests.size() + " failed.");
    }
    
    public static boolean preconditionFailure(final String testName, final String failureMessage, final Runnable test) {
        printTest(testName);

        try {
            test.run();
        } catch(final IllegalArgumentException exception) {
            printSuccess();
            return true;
        }

        printFailure(failureMessage);
        return false;
    }

    public static boolean preconditionSuccess(final String testName, final String failureMessage, final Runnable test) {
        printTest(testName);

        try {
            test.run();
        } catch(final IllegalArgumentException exception) {
            printFailure(failureMessage);
            return false;
        }

        printSuccess();
        return true;
    }

    public static <V> boolean expectValue(final String testName, final V value, final Supplier<V> test) {
        printTest(testName);

        final V testValue = test.get();

        return wrapOutcome(value.equals(testValue), "test value did not match returned value. Supplied value is '" + testValue.toString() + "'");
    }

    public static <V> boolean checkValue(final String testName, final Predicate<V> check, final Supplier<V> test) {
        return checkValue(testName, check, v -> v.toString(), test);
    }

    public static <V> boolean checkValue(final String testName, final Predicate<V> check, final Function<V, String> toString, final Supplier<V> test) {
        printTest(testName);

        final V testValue = test.get();

        return wrapOutcome(check.test(testValue), "test value did not pass the check. Supplied value is " + toString.apply(testValue));
    }

    private static void printSuccess() {
        System.out.println("passed");
    }

    private static void printFailure(final String failureMessage) {
        System.out.println("failed: " + failureMessage);
    }

    private static boolean wrapOutcome(final boolean outcome, final String failureMessage) {
        if (outcome) {
            printSuccess();
        } else {
            printFailure(failureMessage);
        }

        return outcome;
    }

    private static void printTest(final String testName) {
        System.out.print("\t'" + testName + "' executing...\n\t\t");
    }
}
