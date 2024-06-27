package util.data.trees;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import util.data.algebraic.Prod;

public interface Tree<A> {

    /**
     * @return whether this node is a leaf node (i.e., has no subtrees)
     */
    public boolean isLeaf();

    /**
     * @return whether this node is a branch node (i.e., has subtrees)
     */
    public boolean isBranch();
    
    /**
     * @return the underlying value at this node
     */
    public A getNode();

    /**
     * @return the subtrees at this node
     */
    public Collection<? extends Tree<A>> getSubTrees();

    /**
     * Creates a new tree by transforming this tree's values via the provided function.
     * Structurally, the tree should be identical.
     * 
     * @param <B> the target type of the mapped data
     * @param function the function mapping this tree's data to the transformed target type
     * @return a new tree with mapped data and identical structure
     */
    public <B> Tree<B> map(final Function<A, B> function);

    /**
     * Eliminates this list by folding on the left.
     * 
     * @param <B> the output type
     * @param accumulator takes a node argument and a list of the folded subtrees and creates a new output object
     * @return the eliminated tree object
     */
    public <B> B foldl(final Function<Prod<A, List<B>>, B> accumulator);

    /**
     * @return returns a linear (depth-first search) list of this tree's node values
     */
    public List<A> linearize();
}
