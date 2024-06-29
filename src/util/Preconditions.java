package util;

import java.util.Collection;
import java.util.function.Predicate;

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

    public static final <V> void throwIfSatisfied(final V value, final String argumentName, final String conditionDescription, final Predicate<V> condition) {
        if (condition.test(value)) {
            throw new IllegalArgumentException("Argument '" + argumentName + "' satisfied the condition: " + conditionDescription);
        }
    }
}
