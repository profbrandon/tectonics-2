package util.math.instances.doubles.linear;

import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.VecD;
import util.math.vectorspaces.FiniteVectorSpace;
import util.math.vectorspaces.LinearMapSpace;

public abstract class LinearD<N extends Cardinal, M extends Cardinal> 
    extends LinearMapSpace<HomTuple<N, Double>, HomTuple<M, Double>, Double>
    implements FiniteVectorSpace<Exp<HomTuple<N, Double>, HomTuple<M, Double>>, Double> {
    
    public LinearD(final VecD<N> domainSpace, final VecD<M> targetSpace) {
        super(domainSpace, targetSpace);
    }
}
