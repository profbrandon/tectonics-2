package util.trees;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import util.Prod;

public class HomogenousTree<A> implements Tree<A> {

    private final A node;

    private final List<HomogenousTree<A>> subTrees;

    public HomogenousTree(final A node) {
        this.node = node;
        this.subTrees = List.of();
    }

    public HomogenousTree(final A node, final Collection<HomogenousTree<A>> subTrees) {
        this.node = node;
        this.subTrees = List.copyOf(subTrees);
    }

    @Override
    public final A getNode() {
        return this.node;
    }

    @Override
    public final Collection<HomogenousTree<A>> getSubTrees() {
        return this.subTrees;
    }

    @Override
    public final <B> HomogenousTree<B> map(final Function<A, B> function) {
        if (subTrees.isEmpty()) {
            return new HomogenousTree<B>(function.apply(this.node));
        } else {
            return new HomogenousTree<B>(function.apply(this.node), this.subTrees.stream().map(tree -> tree.map(function)).toList());
        }
    }

    public final A foldl(final Function<Prod<A, A>, A> accumulator) {
        if (subTrees.isEmpty()) {
            return this.node;
        } else {
            A result = this.node;

            for (final HomogenousTree<A> subTree : this.subTrees) {
                result = accumulator.apply(Prod.pair(result, subTree.foldl(accumulator)));
            }

            return result;
        }
    }

    public final Collection<A> linearize() {
        return this
            .map(a -> List.of(a))
            .foldl(pair -> Stream.concat(pair.first().stream(), pair.second().stream()).toList());
    }
}
