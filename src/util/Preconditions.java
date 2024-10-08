package util;

import java.util.Collection;
import java.util.function.Function;

public final class Preconditions {
    
    public static final <V> void throwIfNull(final V value, final String argumentName) {
        if (value == null) {
            throw new IllegalArgumentException("Argument '" + argumentName + "' was null.");
        }
    }

    public static final <V> void throwIfContainsNull(final Collection<V> values, final String argumentName) {
        Preconditions.throwIfNull(values, argumentName);

        if (values.stream().anyMatch(v -> v == null)) {
            throw new IllegalArgumentException("Argument collection '" + argumentName + "' contained null values.");
        }
    }

    public static final <V> void throwIfDifferent(final V value1, final V value2, final String message) {
        throwIfSatisfies(value1, a -> !a.equals(value2), message);
    }

    public static final <V> void throwIfEquals(final V value1, final V value2, final String message) {
        throwIfSatisfies(value1, a -> a.equals(value2), message);
    }

    public static final <V> void throwIfSatisfies(final V value, final Function<V, Boolean> predicate, final String message) {
        if (predicate.apply(value)) {
            throw new IllegalArgumentException(message);
        }
    }
}
