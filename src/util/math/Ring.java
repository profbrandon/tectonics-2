package util.math;

public interface Ring<R> extends Group<R> {
    
    public R mult(final R r1, final R r2);
}
