package util.math.vectorspaces;

import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Field;
import util.math.Ring;

public class EndoSpace<V, K> extends LinearMapSpace<V, V, K> implements Ring<Exp<V, V>> {

    public EndoSpace(final VectorSpace<V, K> targetSpace) {
        super(targetSpace, targetSpace);
    }

    @Override
    public Field<K> underlyingField() {
        return targetVectorSpace().underlyingField();
    }

    @Override
    public Exp<V, V> unit() {
        return Exp.identity();
    }

    @Override
    public Exp<V, V> mult(final Exp<V, V> r1, final Exp<V, V> r2) {
        return r1.after(r2);
    }
    
    /**
     * Extremely weak form of equality
     */
    @Override
    public boolean equiv(final Exp<V, V> a1, final Exp<V, V> a2) {
        return a1.equalsExp(a2);
    }

    public static <V, K> Prod<HomTuple<One, V>, HomTuple<One, Exp<V, K>>> toTensor(final Exp<V, V> linear) {
        // TODO: Implement
        return null;
    }
}
