package util.math.instances.doubles.covectors;

import java.util.List;

import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.vectors.VecD;
import util.math.vectorspaces.finite.FiniteDualSpace;

/**
 * Class to represent {@code N}-tuple dual vectors over the {@link DoubleField}.
 */
public abstract class CoVecD<N extends Cardinal> 
    extends
        FiniteDualSpace<HomTuple<N, Double>, Double> {

    /**
     * Constructs the dual vector space of the given double-valued vector space.
     * 
     * @param underlyingSpace the underlying double-valued vector space
     */
    protected CoVecD(final VecD<N> underlyingSpace) {
        super(underlyingSpace);
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<N, Double>, Double>>> decompose(final Exp<HomTuple<N, Double>, Double> v) {
        return domainVectorSpace()
            .basis()
            .stream()
            .map(b -> Prod.pair(transform(v, b), covectorFromBasisVector(b)))
            .toList();
    }
}
