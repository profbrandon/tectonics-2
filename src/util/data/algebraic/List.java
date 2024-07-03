package util.data.algebraic;

import java.util.Collection;

import util.Preconditions;

public class List<A> {
    
    private final Sum<Unit, Prod<A, List<A>>> list;

    private List(final Sum<Unit, Prod<A, List<A>>> list) {
        this.list = list;
    }

    public Sum<Unit, A> head() {
        return Sum.map(
            this.list,
            unit -> unit,
            pair -> pair.first());
    }

    public List<A> tail() {
        return this.list.match(
            unit -> List.empty(),
            pair -> pair.second());
    }

    public static <A> List<A> empty() {
        return new List<>(generateEmpty());
    }

    public static <A> List<A> list(final Collection<A> as) {
        Preconditions.throwIfNull(as, "as");
        Preconditions.throwIfContainsNull(as, "as");

        Sum<Unit, Prod<A, List<A>>> temp = generateEmpty();
        
        for (final A a : as) {
            temp = generateCons(a, new List<>(temp));
        }

        return new List<>(temp);
    }

    private static <A> Sum<Unit, Prod<A, List<A>>> generateEmpty() {
        return Sum.left(Unit.unit());
    }

    private static <A> Sum<Unit, Prod<A, List<A>>> generateCons(final A a, final List<A> as) {
        return Sum.right(Prod.pair(a, as));
    }
}
