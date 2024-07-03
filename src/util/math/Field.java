package util.math;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;

public interface Field<Q> extends Ring<Q>, VectorSpace<Q, Q> {
    
    public Sum<Unit, Q> inv(final Q q);
}
