package util.math;

import util.data.algebraic.Sum;
import util.data.algebraic.Unit;

public interface Field<Q> extends Ring<Q> {
    
    public Q zero();

    public Q unit();

    public Q sum(final Q q1, final Q q2);

    public Q neg(final Q q);

    public Q mult(final Q q1, final Q q2);

    public Sum<Unit, Q> inv(final Q q);
}
