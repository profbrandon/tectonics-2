package util;

public abstract class Equiv<A> {
    public abstract boolean equiv(final A other);

    public final static <X> Equiv<X> fromJavaEquals(final X value) {
        Preconditions.throwIfNull(value, "value");

        return new Equiv<>() {
            @Override
            public boolean equiv(final X other) {
                Preconditions.throwIfNull(other, "other");
                return value.equals(other);
            }
        };
    }
}
