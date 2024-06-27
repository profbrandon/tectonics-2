package test.util.data.trees;

import java.util.List;

import util.data.trees.HomogeneousTree;
import util.testing.UnitTest;

public class HomogeneousTreeTest extends UnitTest {

    private static final List<HomogeneousTree<Integer>> EXAMPLE_SUBTREES_1 = List.of(
        new HomogeneousTree<>(1),
        new HomogeneousTree<>(2),
        new HomogeneousTree<>(3),
        new HomogeneousTree<>(4));

    private static final List<HomogeneousTree<Integer>> EXAMPLE_SUBTREES_2 = List.of(
        new HomogeneousTree<>(6),
        new HomogeneousTree<>(7),
        new HomogeneousTree<>(
            8,
            List.of(new HomogeneousTree<>(9))));

    private static final HomogeneousTree<Integer> EXAMPLE_TREE = new HomogeneousTree<>(
        -1,
        List.of(
            new HomogeneousTree<>(0, EXAMPLE_SUBTREES_1),
            new HomogeneousTree<>(5, EXAMPLE_SUBTREES_2)));
    
    private HomogeneousTreeTest() {
        super("HomogeneousTree Test");
    }

    public static void main(final String[] args) {
        final HomogeneousTreeTest unitTest = new HomogeneousTreeTest();

        unitTest.addTest(HomogeneousTreeTest::leafConstructorOnNull);
        unitTest.addTest(HomogeneousTreeTest::branchConstructorOnNulls);
        unitTest.addTest(HomogeneousTreeTest::isLeafTest);
        unitTest.addTest(HomogeneousTreeTest::isBranchTest);
        unitTest.addTest(HomogeneousTreeTest::getNodeTest);
        unitTest.addTest(HomogeneousTreeTest::getSubTreesOnLeaf);
        unitTest.addTest(HomogeneousTreeTest::getSubTreesOnBranch);
        unitTest.addTest(HomogeneousTreeTest::linearizeTest);
        unitTest.addTest(HomogeneousTreeTest::mapOnNull);
        unitTest.addTest(HomogeneousTreeTest::mapTest);
        unitTest.addTest(HomogeneousTreeTest::foldlOnNull);
        unitTest.addTest(HomogeneousTreeTest::foldlTest);

        unitTest.runTests();
    }

    private static boolean leafConstructorOnNull() {
        return UnitTest.preconditionFailure(
            "Leaf constructor on null",
            "failed to raise exception on null argument",
            () -> new HomogeneousTree<>(null));
    }

    private static boolean branchConstructorOnNulls() {
        return 
            UnitTest.preconditionFailure(
                "Branch constructor on first null argument",
                "failed to raise exception on null first argument",
                () -> new HomogeneousTree<>(null, List.of())) &&
            UnitTest.preconditionFailure(
                "Branch constructor on second null argument",
                "failed to raise exception on null second argument",
                () -> new HomogeneousTree<>(1, null));
    }

    private static boolean isLeafTest() {
        return 
            UnitTest.expectValue(
                "Test isLeaf() on leaf", 
                true, 
                () -> new HomogeneousTree<>(1).isLeaf()) &&
            UnitTest.expectValue(
                "Test isLeaf() on branch", 
                false, 
                () -> new HomogeneousTree<>(2, List.of(new HomogeneousTree<>(1))).isLeaf());
    }

    private static boolean isBranchTest() {
        return
            UnitTest.expectValue(
                "Test isBranch() on branch", 
                true, 
                () -> new HomogeneousTree<>("x", List.of(new HomogeneousTree<>("y"))).isBranch()) &&
            UnitTest.expectValue(
                "Test isBranch() on leaf",
                false,
                () -> new HomogeneousTree<>("z").isBranch());
    }

    private static boolean getNodeTest() {
        return
            UnitTest.expectValue(
                "Test getNode() on leaf",
                true,
                () -> new HomogeneousTree<>(true).getNode()) &&
            UnitTest.expectValue(
                "Test getNode() on branch",
                2f,
                () -> new HomogeneousTree<>(2f, List.of(new HomogeneousTree<>(0f))).getNode());
    }

    private static boolean getSubTreesOnLeaf() {
        return UnitTest.expectValue(
            "Check for subtrees on a leaf node",
            true,
            () -> new HomogeneousTree<>('a').getSubTrees().isEmpty());
    }

    private static boolean getSubTreesOnBranch() {
        return UnitTest.checkValue(
            "Check for subtrees on a branch",
            trees -> trees.getSubTrees().stream().allMatch(subTree -> EXAMPLE_SUBTREES_1.get(subTree.getNode() - 1).getNode() == subTree.getNode()),
            () -> new HomogeneousTree<>(0, EXAMPLE_SUBTREES_1));
    }

    private static boolean linearizeTest() {
        return UnitTest.checkValue(
            "Linearize tree",
            list -> {
                for (int i = -1; i < list.size() - 1; ++i) {
                    if (i != list.get(i + 1)) return false;
                }
                return true;
            },
            () -> EXAMPLE_TREE.linearize());
    }

    private static boolean mapOnNull() {
        return UnitTest.preconditionFailure(
            "Map on null function",
            "failed to raise exception on a null argument",
            () -> EXAMPLE_TREE.map(null));
    }

    private static boolean mapTest() {
        return UnitTest.expectValue(
            "Map tree",
            11 * (11 + 1) / 2,
            () -> EXAMPLE_TREE
                .map(i -> i + 2)
                .foldl(pair -> pair.destroy(
                    i ->
                        l -> i + l.stream().reduce((a, b) -> a + b).orElse(0))));
    }

    private static boolean foldlOnNull() {
        return UnitTest.preconditionFailure(
            "Fold left on null",
            "failed to raise exception on null argument",
            () -> EXAMPLE_TREE.foldl(null));
    }

    private static boolean foldlTest() {
        return UnitTest.expectValue(
            "Fold left tree",
            "abcdefghijk",
            () -> EXAMPLE_TREE
                .map(i -> (char) ('a' + i + 1))
                .foldl(pair -> pair.destroy(
                    i ->
                        l -> i + l.stream().reduce((a, b) -> a + b).orElse(""))));
    }
}
