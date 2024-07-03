package util.math.instances.doubles;

import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.FiniteVectorSpace;
import util.math.vectorspaces.LinearMapSpace;

public abstract class LinearD<N extends Ordinal, M extends Ordinal> 
    extends LinearMapSpace<HomTuple<N, Double>, HomTuple<M, Double>, Double>
    implements FiniteVectorSpace<Exp<HomTuple<N, Double>, HomTuple<M, Double>>, Double> {
    
    public LinearD(final VecD<N> domainSpace, final VecD<M> targetSpace) {
        super(domainSpace, targetSpace);
    }
}
