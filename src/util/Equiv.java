package util;

public abstract class Equiv<A> {
    public abstract boolean equiv(final A other);

    public final static <X> Equiv<X> toEquiv(final X value) {
        return new Equiv<>() {
            @Override
            public boolean equiv(X other) {
                return value.equals(other);
            }
        };
    }
}
