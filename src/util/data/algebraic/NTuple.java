package util.data.algebraic;

import java.util.function.Function;

import util.counting.Ordinal;
import util.counting.OrdinalSet;

public class NTuple<O extends Ordinal, A> {

    private final Function<OrdinalSet<O>, A> elements;
    
    public NTuple(final Function<OrdinalSet<O>, A> elements) {
        this.elements = elements;
    }

    public A at(final OrdinalSet<O> index) {
        return this.elements.apply(index);
    }
}
