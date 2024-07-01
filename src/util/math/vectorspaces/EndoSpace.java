package util.math.vectorspaces;

import util.counting.Ordinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.VectorSpace;

public abstract class EndoSpace<V, K> extends LinearMapSpace<V, V, K> {

    public EndoSpace(final VectorSpace<V, K> targetSpace) {
        super(targetSpace);
    }
    
    public static <V, K> Prod<HomTuple<One, V>, HomTuple<One, Exp<V, K>>> toTensor(final Exp<V, V> linear) {
        // TODO: Implement
        return null;
    }
}
