package util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class DistinguishedTree<N,A> implements Tree<Either<N,A>> {

    private final Either<Prod<N,List<DistinguishedTree<N,A>>>, A> node;

    public DistinguishedTree(final A node) {
        this.node = Either.right(node);
    }

    public DistinguishedTree(final N node, final Collection<DistinguishedTree<N,A>> subTrees) {
        this.node = Either.left(Prod.pair(node, List.copyOf(subTrees)));
    }

    @Override
    public final Either<N,A> getNode() {
        return this.node.match(
            pair -> pair.destroy(n -> { return (subTrees -> Either.left(n)); }),
            a -> Either.right(a));
    }

    @Override
    public final Collection<DistinguishedTree<N,A>> getSubTrees() {
        return this.node.match(pair -> pair.second(), a -> List.of());
    }

    public final <B> DistinguishedTree<N, B> mapLeaves(final Function<A, B> function) {
        return this.node.match(
            pair -> new DistinguishedTree<>(pair.first(), pair.second().stream().map(tree -> tree.mapLeaves(function)).toList()),
            a -> new DistinguishedTree<>(function.apply(a)));
    }

    public final <M> DistinguishedTree<M, A> mapBranches(final Function<N, M> function) {
        return this.node.match(
            pair -> new DistinguishedTree<>(function.apply(pair.first()), pair.second().stream().map(tree -> tree.mapBranches(function)).toList()),
            a -> new DistinguishedTree<>(a));
    }

    @Override
    public final <B> HomogenousTree<B> map(final Function<Either<N, A>, B> function) {
        return this.node.match(
            pair -> new HomogenousTree<B>(function.apply(Either.left(pair.first())), pair.second().stream().map(tree -> tree.map(function)).toList()),
            a -> new HomogenousTree<B>(function.apply(Either.right(a))));
    }

    public final boolean isLeaf() {
        return node.match(pair -> false, a -> true);
    }

    public final boolean isBranch() {
        return node.match(pair -> true, a -> false);
    }
}