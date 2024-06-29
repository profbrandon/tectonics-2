package util.data.algebraic;

import java.util.function.Function;

import util.counting.Ordinal;
import util.counting.OrdinalSet;

public class HomTuple<O extends Ordinal, A> {

    private final Function<OrdinalSet<O>, A> elements;
    
    public HomTuple(final Function<OrdinalSet<O>, A> elements) {
        this.elements = elements;
    }

    public A at(final OrdinalSet<O> index) {
        return this.elements.apply(index);
    }

    public <B> HomTuple<O, B> mapAll(final Function<A, B> function) {
        return new HomTuple<>(function.compose(this.elements));
    }

    public <B> HomTuple<O, B> mapEach(final Function<OrdinalSet<O>, Function<A, B>> function) {
        return new HomTuple<>(
            ordSet -> 
                function
                    .apply(ordSet) // Extract nth function
                    .apply(this.elements.apply(ordSet))); // Extract nth value and feed it to nth function
    }

    public <B> HomTuple<O, B> mapEach(final HomTuple<O, Function<A, B>> funTuple) {
        return this.mapEach(funTuple.elements);
    }
}
