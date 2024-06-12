package util;

public final class Preconditions {
    
    public static final <V> void throwIfNull(final V value, final String argumentName) {
        if (value == null) {
            throw new IllegalArgumentException("Argument '" + argumentName + "' was null.");
        }
    }
}
