package util.math.instances.doubles.linear;

import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.VecD;
import util.math.vectorspaces.FiniteVectorSpace;
import util.math.vectorspaces.LinearMapSpace;

/**
 * Class to represent linear transformations from a vector space of {@code N}-tuples of {@link Double}s to
 * a space of {@code M}-tuples of {@link Double}s.
 */
public abstract class LinearD<N extends Cardinal, M extends Cardinal> 
    extends 
        LinearMapSpace<HomTuple<N, Double>, HomTuple<M, Double>, Double>
    implements
        FiniteVectorSpace<Exp<HomTuple<N, Double>, HomTuple<M, Double>>, Double> {
    
    /**
     * Constructs a linear map space from the domain space to the target space, where both
     * are Double-valued vector spaces. This class operates on linear transformations of
     * dimension {@code N*M}.
     * 
     * @param domainSpace the domain {@code N}-dimensional Double vector space
     * @param targetSpace the target {@code M}-dimensional Double vector space
     */
    public LinearD(final VecD<N> domainSpace, final VecD<M> targetSpace) {
        super(domainSpace, targetSpace);
    }
}
