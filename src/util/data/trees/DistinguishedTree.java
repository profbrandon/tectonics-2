package util.data.trees;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import util.data.algebraic.Sum;
import util.Preconditions;
import util.data.algebraic.Identities;
import util.data.algebraic.Prod;

/**
 * Class to represent a {@link Tree} that distinguishes its leaf nodes from its branch nodes.
 * Although this class implements {@link Tree} over the {@link Sum} of two type parameters,
 * not every node can have either a {@link N} type or {@link A} type. For the {@link DistinguishedTree},
 * branch nodes are restricted to be of type {@link N} and leaf nodes are restricted to be of
 * type {@link A}, however, this can easily be converted to a homogeneous tree of the 
 * aformentioned type by calling {@link Tree#map(Function)}. If one wants to map a {@link DistinguishedTree}
 * to another, then they must call {@link DistinguishedTree#mapBoth(Function, Function)}.
 */
public class DistinguishedTree<N,A> implements Tree<Sum<N,A>> {

    // Either a branch of type N with subtrees or a leaf with type A: (N x List<D<N,A>>) + A
    private final Sum<Prod<N,List<DistinguishedTree<N,A>>>, A> node;

    /**
     * Creates a leaf tree.
     * 
     * @param node the leaf node's value
     */
    public DistinguishedTree(final A node) {
        Preconditions.throwIfNull(node, "node");

        this.node = Sum.right(node);
    }

    /**
     * Creates a branch tree.
     * 
     * @param node the branch node's value
     * @param subTrees the branch node's subtrees
     */
    public DistinguishedTree(final N node, final Collection<DistinguishedTree<N,A>> subTrees) {
        Preconditions.throwIfNull(node, "node");
        Preconditions.throwIfContainsNull(subTrees, "subTrees");
                
        this.node = Sum.left(Prod.pair(node, List.copyOf(subTrees)));
    }

    /**
     * Creates a new {@link DistinguishedTree} in which all of the leaf and branch nodes are mapped via the
     * provided functions. The new tree should have the same structure.
     * 
     * @param <M> the new branch node type
     * @param <B> the new leaf node type
     * @param branchMap the function to map the old branches to new branches
     * @param leafMap the function to map the old leaves to the new leaves
     * @return a new {@link DistinguishedTree} with new leaves and branches mapped via the provided functions
     */
    public final <M, B> DistinguishedTree<M, B> mapBoth(final Function<N, M> branchMap, final Function<A, B> leafMap) {
        Preconditions.throwIfNull(branchMap, "branchMap");
        Preconditions.throwIfNull(leafMap, "leafMap");
        return this.node.match(
            branch -> branch.destroy(
                n -> 
                    subTrees ->
                        new DistinguishedTree<>(
                            branchMap.apply(n), 
                            subTrees.stream().map(s -> s.mapBoth(branchMap, leafMap)).toList())),
            leaf -> new DistinguishedTree<>(leafMap.apply(leaf)));
    }

    /**
     * Deterimines if this {@link DistinguishedTree} is equivalent to the given one. At the lowest level, it
     * uses {@link Object#equals(Object)} to determine if the node values are equal. Trees must be structurally
     * equivalent to be considered for value equivalence.
     * 
     * @param other the other distinguished tree
     * @return whether the two trees are equal (up to structural equivalence and {@link Object#equals(Object)})
     */
    public final boolean equalsDistinguishedTree(final DistinguishedTree<N, A> other) {
        final Sum<A, Prod<N, List<DistinguishedTree<N, A>>>> otherSwapped = Identities.sumCommute(other.node);

        // TODO: make this more efficient by checking structure before item equivalence.
        
        return Identities.sumCommute(this.node).match(
            a1 -> otherSwapped.match(
                a2 -> a1.equals(a2),
                p2 -> false),
            p1 -> otherSwapped.match(
                a2 -> false,
                p2 -> {
                    final List<DistinguishedTree<N, A>> list1 = p1.second();
                    final List<DistinguishedTree<N, A>> list2 = p2.second();

                    final boolean looselyEqual = p1.first().equals(p2.first()) && list1.size() == list2.size();

                    if (!looselyEqual) return false;
                    else {
                        for (int i = 0; i < list1.size(); ++i) {
                            if (!list1.get(i).equalsDistinguishedTree(list2.get(i))) return false;
                        }
                        return true;
                    }
                }));
    }

    @Override
    public final boolean isLeaf() {
        return node.match(pair -> false, a -> true);
    }

    @Override
    public final boolean isBranch() {
        return node.match(pair -> true, a -> false);
    }

    @Override
    public final Sum<N,A> getNode() {
        return this.node.match(
            pair -> pair.destroy(n -> { return (subTrees -> Sum.left(n)); }),
            a -> Sum.right(a));
    }

    @Override
    public final Collection<DistinguishedTree<N,A>> getSubTrees() {
        return this.node.match(pair -> pair.second(), a -> List.of());
    }

    @Override
    public final <B> HomogeneousTree<B> map(final Function<Sum<N, A>, B> function) {
        Preconditions.throwIfNull(function, "function");
        return this.node.match(
            pair -> pair.destroy(
                n ->
                    subTrees ->
                        new HomogeneousTree<>(function.apply(Sum.left(n)), subTrees.stream().map(tree -> tree.map(function)).toList())),
            a -> new HomogeneousTree<>(function.apply(Sum.right(a))));
    }

    @Override
    public final List<Sum<N, A>> linearize() {
        return this
            .map(sum -> sum)
            .linearize();
    }

    @Override
    public <B> B foldl(final Function<Prod<Sum<N, A>, List<B>>, B> accumulator) {
        return this
            .map(sum -> sum)
            .foldl(accumulator);
    }

    /**
     * Creates a {@link DistinguishedTree} from a {@link HomogeneousTree}. This is an inverse for the correctly 
     * specified version of {@link DistinguishedTree#map(Function)}.
     * 
     * @param <N> the type of the {@link HomogeneousTree}'s underlying node values
     * @param homogeneousTree the {@link HomogeneousTree} to convert
     * @return a {@link DistinguishedTree} that now distinguishes leaves from branches with both having the same type
     */
    public final static <N> DistinguishedTree<N, N> fromHomogeneous(final HomogeneousTree<N> homogeneousTree) {
        Preconditions.throwIfNull(homogeneousTree, "homogeneousTree");
        return homogeneousTree.isLeaf() ?
            new DistinguishedTree<>(homogeneousTree.getNode()) :
            new DistinguishedTree<>(
                homogeneousTree.getNode(), 
                homogeneousTree.getSubTrees()
                    .stream()
                    .map(DistinguishedTree::fromHomogeneous)
                    .toList());
    }
}