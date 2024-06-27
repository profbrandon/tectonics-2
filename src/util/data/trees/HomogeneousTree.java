package util.data.trees;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import util.Preconditions;
import util.data.algebraic.Prod;

/**
 * Class to represent a {@link Tree} with branches and nodes of the same type (hence homogeneous). 
 */
public class HomogeneousTree<A> implements Tree<A> {

    private final A node;
    private final List<HomogeneousTree<A>> subTrees;

    /**
     * Constructs a leaf tree.
     * 
     * @param node the leaf node's value
     */
    public HomogeneousTree(final A node) {
        Preconditions.throwIfNull(node, "node");

        this.node = node;
        this.subTrees = List.of();
    }

    /**
     * Constructs a branch tree consisting of a node and subtrees.
     * 
     * @param node the branch node's value
     * @param subTrees the branch node's subtrees
     */
    public HomogeneousTree(final A node, final Collection<HomogeneousTree<A>> subTrees) {
        Preconditions.throwIfNull(node, "node");
        Preconditions.throwIfContainsNull(subTrees, "subTrees");

        this.node = node;
        this.subTrees = List.copyOf(subTrees);
    }

    @Override
    public final boolean isLeaf() {
        return this.subTrees.isEmpty();
    }

    @Override
    public final boolean isBranch() {
        return !this.subTrees.isEmpty();
    }

    @Override
    public final A getNode() {
        return this.node;
    }

    @Override
    public final Collection<HomogeneousTree<A>> getSubTrees() {
        return this.subTrees;
    }

    @Override
    public final <B> HomogeneousTree<B> map(final Function<A, B> function) {
        Preconditions.throwIfNull(function, "function");

        if (subTrees.isEmpty()) {
            return new HomogeneousTree<B>(function.apply(this.node));
        } else {
            return new HomogeneousTree<B>(function.apply(this.node), this.subTrees.stream().map(tree -> tree.map(function)).toList());
        }
    }

    @Override
    public final List<A> linearize() {
        return this.foldl(
            pair -> pair.<List<A>>destroy(
                a -> 
                    list -> 
                        Stream.concat(Stream.of(List.of(a)), list.stream()).flatMap(l -> l.stream()).toList()));
    }

    @Override
    public final <B> B foldl(final Function<Prod<A, List<B>>, B> accumulator) {
        Preconditions.throwIfNull(accumulator, "accumulator");
        
        if (this.isLeaf()) {
            return accumulator.apply(Prod.pair(this.node, List.of()));
        } else {
            return accumulator.apply(Prod.pair(this.node, subTrees.stream().map(subTree -> subTree.foldl(accumulator)).toList()));
        }
    }
}
