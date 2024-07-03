package util.math;

public interface Monoid<M> extends Equiv<M> {
    
    public M zero();

    public M sum(final M m1, final M m2);
}
