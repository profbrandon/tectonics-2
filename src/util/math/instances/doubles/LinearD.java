package util.math.instances.doubles;

import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.vectorspaces.LinearMapSpace;

public class LinearD<N extends Ordinal, M extends Ordinal> extends LinearMapSpace<HomTuple<N, Double>, HomTuple<M, Double>, Double> {
    public LinearD(final VecD<M> targetSpace) {
        super(targetSpace);
    }
}
