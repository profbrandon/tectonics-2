package util.math;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;
import util.math.vectorspaces.VectorSpace;

public interface Field<Q> extends Ring<Q>, VectorSpace<Q, Q> {
    
    public Sum<Unit, Q> inv(final Q q);
}
