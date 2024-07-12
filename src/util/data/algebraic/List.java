package util.data.algebraic;

import java.util.Collection;
import java.util.function.Function;

import util.Preconditions;

/**
 * Class to represent a recursive list algebraic data type.
 */
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
            __   -> List.empty(),
            pair -> pair.second());
    }

    public boolean isEmpty() {
        return this.match(__ -> true, a -> as -> false);
    }

    public <U> U match(final Function<Unit, U> onEmpty, final Function<A, Function<List<A>, U>> onCons) {
        Preconditions.throwIfNull(onEmpty, "onEmpty");
        Preconditions.throwIfNull(onCons, "onCons");
        return this.list.match(
            unit -> onEmpty.apply(unit),
            pair -> onCons.apply(pair.first()).apply(pair.second()));
    }

    public <B> List<B> map(final Function<A, B> fun) {
        Preconditions.throwIfNull(fun, "fun");
        return this.match(
            __      -> List.empty(),
            a -> as -> List.cons(fun.apply(a), as.map(fun)));
    }

    public <U> U foldr(final U seed, final Function<A, Function<U, U>> fun) {
        Preconditions.throwIfNull(seed, "seed");
        Preconditions.throwIfNull(fun, "fun");
        return this.match(
            __      -> seed,
            a -> as -> fun.apply(a).apply(as.foldr(seed, fun)));
    }

    public <B> List<Prod<A, B>> zip(final List<B> other) {
        Preconditions.throwIfNull(other, "other");
        return this.match(
            __      -> List.empty(),
            a -> as -> other.match(
                    __      -> List.empty(),
                    b -> bs -> List.cons(Prod.pair(a, b), as.zip(bs))));
    }

    public List<A> reverse() {
        return List.accumulate(Prod.pair(this, List.empty()), a -> rs -> cons(a, rs)).second();
    }

    public List<A> concat(final List<A> other) {
        return List.accumulate(Prod.pair(this.reverse(), other), a -> rs -> cons(a, rs)).second();
    }

    public String asString(final Function<A, String> convertToString) {
        return this.match(
            __ -> "[]",
            a -> as -> "[" + bodyToString(cons(a, as), convertToString) + "]");
    }

    @Override
    public String toString() {
        return this.asString(Object::toString);
    }

    public static <A> List<A> empty() {
        return new List<>(generateEmpty());
    }

    public static <A> List<A> cons(final A head, final List<A> tail) {
        Preconditions.throwIfNull(head, "head");
        Preconditions.throwIfNull(tail, "tail");
        return new List<>(generateCons(head, tail));
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

    public static <A, U> U match(final List<A> list, final Function<Unit, U> onEmpty, final Function<A, Function<List<A>, U>> onCons) {
        Preconditions.throwIfNull(list, "list");
        return list.match(onEmpty, onCons);
    }

    public static <A, B> List<B> map(final List<A> list, final Function<A, B> fun) {
        Preconditions.throwIfNull(list, "list");
        return list.map(fun);
    }

    public static <A, B, U> U foldr(final List<A> list, final U seed, final Function<A, Function<U, U>> fun) {
        Preconditions.throwIfNull(list, "list");
        return list.foldr(seed, fun);
    }

    public static <A, B> List<Prod<A, B>> zip(final List<A> listA, final List<B> listB) {
        Preconditions.throwIfNull(listA, "listA");
        return listA.zip(listB);
    }

    public static <A> List<A> produce(final A a) {
        return List.cons(a, List.empty());
    }

    public static <A> List<A> join(final List<List<A>> mma) {
        return mma.foldr(List.empty(), l1 -> l2 -> l1.concat(l2));
    }

    private static <A> Sum<Unit, Prod<A, List<A>>> generateEmpty() {
        return Sum.left(Unit.unit());
    }

    private static <A> Sum<Unit, Prod<A, List<A>>> generateCons(final A a, final List<A> as) {
        return Sum.right(Prod.pair(a, as));
    }

    private static <A, U> Prod<List<A>, U> accumulate(final Prod<List<A>, U> stack, final Function<A, Function<U, U>> fun) {
        return
            stack.destroy(
                as -> 
                    u -> as.match(
                        __         -> Prod.pair(List.empty(), u),
                        a -> atail -> List.accumulate(Prod.pair(atail, fun.apply(a).apply(u)), fun)));
    }

    private static <A> String bodyToString(final List<A> list, final Function<A, String> convertToString) {
        return list.match(
            __       -> "",
            a -> as -> convertToString.apply(a) + as.match(
                __      -> "", 
                b -> __ -> ", " + bodyToString(as, convertToString)));
    }
}
