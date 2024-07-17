package util;

import java.util.function.Function;

public final class Functional {
    
    public static final <A, U> U let(final A value, final Function<A, U> expression) {
        return expression.apply(value);
    }
}
