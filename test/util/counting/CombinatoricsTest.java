package test.util.counting;

import java.util.List;

import util.counting.Combinatorics;
import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.testing.UnitTest;

public final class CombinatoricsTest extends UnitTest {
    
    private CombinatoricsTest() {
        super("Combinatorics Testing");
    }

    public static void main(final String[] args) {
        final CombinatoricsTest unitTest = new CombinatoricsTest();

        unitTest.addTest(CombinatoricsTest::nProductInteger1On2);
        unitTest.addTest(CombinatoricsTest::nProductInteger2On2);
        unitTest.addTest(CombinatoricsTest::nProductInteger3On2);
        unitTest.addTest(CombinatoricsTest::nProductInteger2On3);
        unitTest.addTest(CombinatoricsTest::nProductOrdinal2On3);
        unitTest.addTest(CombinatoricsTest::cartesianProduct);

        unitTest.runTests();
    }

    public static boolean nProductInteger1On2() {
        return UnitTest.checkAllValues(
            "Check the n-product function for 1-tuples on 2 values", 
            List.of(
                List.of(0), 
                List.of(1)),
            l1 -> l2 -> l1.equals(l2),
            Object::toString,
            () -> Combinatorics.nProduct(1, List.of(0, 1)));
    }

    public static boolean nProductInteger2On2() {
        return UnitTest.checkAllValues(
            "Check the n-product function for 2-tuples on 2 values", 
            List.of(
                List.of(0, 0), 
                List.of(0, 1), 
                List.of(1, 0), 
                List.of(1, 1)),
            l1 -> l2 -> l1.equals(l2),
            Object::toString,
            () -> Combinatorics.nProduct(2, List.of(0, 1)));
    }

    public static boolean nProductInteger3On2() {
        return UnitTest.checkAllValues(
            "Check the n-product function for 3-tuples on 2 values", 
            List.of(
                List.of(0, 0, 0),
                List.of(0, 0, 1), 
                List.of(0, 1, 0),
                List.of(0, 1, 1), 
                List.of(1, 0, 0),
                List.of(1, 0, 1), 
                List.of(1, 1, 0),
                List.of(1, 1, 1)),
            l1 -> l2 -> l1.equals(l2),
            Object::toString,
            () -> Combinatorics.nProduct(3, List.of(0, 1)));
    }

    public static boolean nProductInteger2On3() {
        return UnitTest.checkAllValues(
            "Check the n-product function for 2-tuples on 3 values", 
            List.of(
                List.of(0, 0),
                List.of(0, 1), 
                List.of(0, 2),
                List.of(1, 0), 
                List.of(1, 1),
                List.of(1, 2), 
                List.of(2, 0),
                List.of(2, 1),
                List.of(2, 2)),
            l1 -> l2 -> l1.equals(l2),
            Object::toString,
            () -> Combinatorics.nProduct(2, List.of(0, 1, 2)));
    }

    public static boolean nProductOrdinal2On3() {
        return UnitTest.checkAllValues(
            "Check the n-product ordinal function for 2-tuples on 3 values",
            List.of(
                HomTuple.tuple(0, 0),
                HomTuple.tuple(0, 1), 
                HomTuple.tuple(0, 2),
                HomTuple.tuple(1, 0), 
                HomTuple.tuple(1, 1),
                HomTuple.tuple(1, 2), 
                HomTuple.tuple(2, 0),
                HomTuple.tuple(2, 1),
                HomTuple.tuple(2, 2)),
            l1 -> l2 -> l1.equalsTuple(Ordinal.TWO_SET, l2),
            Object::toString,
            () -> Combinatorics.nProduct(List.copyOf(Ordinal.TWO_SET), List.of(0, 1, 2)));
    }

    public static boolean cartesianProduct() {
        return UnitTest.checkAllValues(
            "Check the cartesian product",
            List.of(
                Prod.pair(1, true),
                Prod.pair(1, false),
                Prod.pair(2, true),
                Prod.pair(2, false),
                Prod.pair(3, true),
                Prod.pair(3, false)
            ),
            a -> b -> a.equalsProd(b),
            Object::toString,
            () -> Combinatorics.cartesianProduct(List.of(1, 2, 3), List.of(true, false)));
    }
}
