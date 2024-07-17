package util.math.instances.doubles.covectors;

import util.counting.Cardinal;
import util.data.algebraic.HomTuple;
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
}
