package util.trees;

import java.util.Collection;
import java.util.List;

public class LabeledTree<A> extends DistinguishedTree<String, A> {
    
    public LabeledTree(final A value) {
        super(value);
    }

    public LabeledTree(final String label, final Collection<LabeledTree<A>> subTrees) {
        super(label, List.copyOf(subTrees));
    }

    
}
