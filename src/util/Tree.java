package util;

import java.util.Collection;
import java.util.function.Function;

public interface Tree<A> {
    
    public A getNode();

    public Collection<? extends Tree<A>> getSubTrees();

    public <B> Tree<B> map(final Function<A, B> function);
}
