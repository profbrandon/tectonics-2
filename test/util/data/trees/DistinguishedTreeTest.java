package test.util.data.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.data.trees.DistinguishedTree;
import util.testing.UnitTest;

public final class DistinguishedTreeTest extends UnitTest {

    private static final DistinguishedTree<Integer, String> EXAMPLE_TREE = new DistinguishedTree<>(
        -1,
        List.of(
            new DistinguishedTree<>("a"),
            new DistinguishedTree<>(
                0, 
                List.of(
                    new DistinguishedTree<>("b"),
                    new DistinguishedTree<>("c"),
                    new DistinguishedTree<>(
                        1, 
                        List.of(new DistinguishedTree<>("d"))),
                    new DistinguishedTree<>("e"))),
            new DistinguishedTree<>(
                2,
                List.of(new DistinguishedTree<>("f")))));
    
    private DistinguishedTreeTest() {
        super("DistinguishedTree Test");
    }

    public static void main(final String[] args) {
        final DistinguishedTreeTest unitTest = new DistinguishedTreeTest();

        unitTest.addTest(DistinguishedTreeTest::leafConstructorOnNull);
        unitTest.addTest(DistinguishedTreeTest::branchConstructorOnNull);
        unitTest.addTest(DistinguishedTreeTest::isLeafTest);
        unitTest.addTest(DistinguishedTreeTest::isBranchTest);
        unitTest.addTest(DistinguishedTreeTest::getNodeTest);
        unitTest.addTest(DistinguishedTreeTest::mapBothOnNull);
        unitTest.addTest(DistinguishedTreeTest::mapBothTest);
        unitTest.addTest(DistinguishedTreeTest::mapBothOnIdentity);
        unitTest.addTest(DistinguishedTreeTest::equalsDistinguishedTreeFailure);
        unitTest.addTest(DistinguishedTreeTest::getSubTreesTest);
        unitTest.addTest(DistinguishedTreeTest::mapOnNull);
        unitTest.addTest(DistinguishedTreeTest::mapAndfromHomogeneous);
        unitTest.addTest(DistinguishedTreeTest::linearizeTest);

        unitTest.runTests();
    }

    private static boolean leafConstructorOnNull() {
        return UnitTest.preconditionFailure(
            "Leaf node constructor on null",
            "failed to raise exception on null arguments",
            () -> new DistinguishedTree<>(null));
    }

    private static boolean branchConstructorOnNull() {
        final List<DistinguishedTree<Integer, Integer>> list = new ArrayList<>();
        list.add(null);

        return
            UnitTest.preconditionFailure(
                "Branch node constructor on null 1st argument",
                "failed to raise exception on null first argument",
                () -> new DistinguishedTree<>(null, List.of())) &&
            UnitTest.preconditionFailure(
                "Branch node constructor on null 2nd argument",
                "failed to raise exception on null second argument", 
                () -> new DistinguishedTree<>(1, null)) &&
            UnitTest.preconditionFailure(
                "Branch node constructor on 2nd argument containing null",
                "failed to raise exception on second argument containing a null value",
                () -> new DistinguishedTree<>(1, list));
    }

    private static boolean isLeafTest() {
        return 
            UnitTest.expectValue(
                "Test isLeaf() on leaf", 
                true, 
                () -> new DistinguishedTree<>(1).isLeaf()) &&
            UnitTest.expectValue(
                "Test isLeaf() on branch", 
                false, 
                () -> new DistinguishedTree<>(2, List.of(new DistinguishedTree<>("abcd"))).isLeaf());
    }

    private static boolean isBranchTest() {
        return
            UnitTest.expectValue(
                "Test isBranch() on branch", 
                true, 
                () -> new DistinguishedTree<>("x", List.of(new DistinguishedTree<>("y"))).isBranch()) &&
            UnitTest.expectValue(
                "Test isBranch() on leaf",
                false,
                () -> new DistinguishedTree<>("z").isBranch());
    }

    private static boolean getNodeTest() {
        return
            UnitTest.checkValue(
                "Test getNode() on leaf",
                v -> v.equalsSum(Sum.right(true)),
                () -> new DistinguishedTree<Integer, Boolean>(true).getNode()) &&
            UnitTest.checkValue(
                "Test getNode() on branch",
                v -> v.equalsSum(Sum.left(2f)),
                () -> new DistinguishedTree<>(2f, List.of(new DistinguishedTree<>(false))).getNode());
    }

    private static boolean mapBothOnNull() {
        return
            UnitTest.preconditionFailure(
                "Test mapBoth() on null 1st argument",
                "failed to raise exception on null first argument",
                () -> EXAMPLE_TREE.mapBoth(null, v -> v)) &&
            UnitTest.preconditionFailure(
                "Test mapBoth() on null 2nd argument",
                "failed to raise exception on null second argument",
                () -> EXAMPLE_TREE.mapBoth(v -> v, null));
    }

    private static boolean mapBothTest() {
        return UnitTest.checkValue(
            "Test mapBoth() function (and foldl)",
            v -> v.equalsProd(Prod.pair(4 * 5 / 2, "a,b,c,d,e,f,")),
            () -> EXAMPLE_TREE
                .mapBoth(number -> number + 2, letter -> letter + ",")
                .<Prod<Integer, String>>foldl(pair -> pair.destroy(
                    sum ->
                        list -> {
                            final Prod<Integer, String> value = list
                                .stream()
                                .reduce(
                                    (pair1, pair2) -> 
                                        Prod.pair(
                                            pair1.first() + pair2.first(),
                                            pair1.second() + pair2.second()))
                                .orElse(Prod.pair(0, ""));
                            return sum.match(
                                i -> Prod.mapFirst(value, v -> v + i),
                                s -> Prod.mapSecond(value, v -> v + s));
                        })));
    }

    private static boolean mapBothOnIdentity() {
        return UnitTest.checkValue(
            "Test mapBoth() function on identity",
            v -> v.equalsDistinguishedTree(EXAMPLE_TREE),
            () -> EXAMPLE_TREE.mapBoth(v -> v, v -> v));
    }

    private static boolean equalsDistinguishedTreeFailure() {
        return UnitTest.checkValue(
            "Test equalsDistinguishedTree() for failure on unequal trees",
            v -> !v.equalsDistinguishedTree(EXAMPLE_TREE),
            () -> EXAMPLE_TREE.mapBoth(i -> i == 2 ? 1000 : i, v -> v));
    }

    private static boolean getSubTreesTest() {
        return UnitTest.checkValue(
            "Test getSubTrees()", 
            v -> v.stream().toList().get(0).equalsDistinguishedTree(new DistinguishedTree<>("f")), 
            () -> EXAMPLE_TREE.getSubTrees().stream().toList().get(2).getSubTrees());
    }

    private static boolean mapOnNull() {
        return UnitTest.preconditionFailure(
            "Test map() on null argument",
            "failed to raise exception on null arugment", 
            () -> EXAMPLE_TREE.map(null));
    }

    private static boolean mapAndfromHomogeneous() {
        final DistinguishedTree<Integer, Integer> dTree = EXAMPLE_TREE.mapBoth(i -> i, s -> (int) s.charAt(0));

        return UnitTest.checkValue(
            "Ensure map() and fromHomogeneous() act as inverses",
            v -> v.equalsDistinguishedTree(dTree),
            () -> DistinguishedTree.fromHomogeneous(EXAMPLE_TREE.map(sum -> sum.match(i -> i, s -> (int) s.charAt(0)))));
    }

    private static boolean linearizeTest() {
        return UnitTest.checkValue(
            "Check that linearize works", 
            v -> v.equalsProd(Prod.pair("0123", "abcdef")),
            () -> {
                final List<Sum<Integer, String>> list = EXAMPLE_TREE.mapBoth(i -> i + 1, s -> s).linearize();

                return Prod.pair(
                    list.stream()
                        .flatMap(
                            sum -> sum.match(
                                i -> Stream.of(i.toString()),
                                s -> Stream.of()))
                        .reduce((s1, s2) -> s1 + s2)
                        .orElse("uh oh"), 
                    list.stream()
                        .flatMap(
                            sum -> sum.match(
                                i -> Stream.of(),
                                s -> Stream.of(s)))
                        .reduce((s1, s2) -> s1 + s2)
                        .orElse("uh oh"));
            });
    }
}
