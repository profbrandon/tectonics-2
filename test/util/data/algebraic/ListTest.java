package test.util.data.algebraic;

import util.data.algebraic.List;
import util.testing.UnitTest;

public final class ListTest extends UnitTest {

    private static final List<Integer> LIST_1 = List.list(1, 2, 3, 4, 5, 6);
    private static final List<Integer> LIST_2 = List.list(7, 8, 9, 10);
    private static final List<Integer> LIST_3 = List.list(11, 12, 13, 14);
    
    private ListTest() {
        super("Algebraic List Test");
    }    

    public static void main(final String[] args) {
        final ListTest unitTest = new ListTest();

        unitTest.addTest(ListTest::emptyList);
        unitTest.addTest(ListTest::emptyListLength);
        unitTest.addTest(ListTest::emptyListString);
        unitTest.addTest(ListTest::listLength);
        unitTest.addTest(ListTest::listString);
        unitTest.addTest(ListTest::reverseList);
        unitTest.addTest(ListTest::concatList);
        unitTest.addTest(ListTest::joinList);

        unitTest.runTests();
    }

    private static boolean emptyList() {
        return UnitTest.expectValue(
            "Is empty list empty",
            true,
            () -> List.empty().isEmpty());
    }

    private static boolean emptyListLength() {
        return UnitTest.expectValue(
            "Is empty list length zero",
            0,
            () -> List.empty().length());
    }

    private static boolean emptyListString() {
        return UnitTest.expectValue(
            "Empty list string",
            "[]",
            () -> List.empty().toString());
    }

    private static boolean listLength() {
        return UnitTest.expectValue(
            "List size test",
            6,
            () -> LIST_1.length());
    }

    private static boolean listString() {
        return UnitTest.expectValue(
            "List string",
            "[1, 2, 3, 4, 5, 6]",
            () -> LIST_1.toString());
    }

    public static boolean reverseList() {
        return UnitTest.checkValue(
            "Reverse list",
            l -> l.equalsList(List.list(6, 5, 4, 3, 2, 1), pair -> pair.first().equals(pair.second())),
            () -> LIST_1.reverse());
    }

    public static boolean concatList() {
        return UnitTest.checkValue(
            "Concatenate lists",
            l -> l.equalsList(
                List.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                pair -> pair.destroy(a -> b -> a.equals(b))),
            () -> LIST_1.concat(LIST_2));
    }

    public static boolean joinList() {
        return UnitTest.checkValue(
            "Join list of lists",
            l -> l.equalsList(
                List.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
                pair -> pair.destroy(a -> b -> a.equals(b))),
            () -> List.join(List.cons(LIST_1, List.cons(LIST_2, List.cons(LIST_3, List.empty())))));
    }
}
