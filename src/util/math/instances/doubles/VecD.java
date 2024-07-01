package util.math.instances.doubles;

import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.FiniteVectorSpace;
import util.math.InnerProductSpace;
import util.math.vectorspaces.NVectorSpace;

public abstract class VecD<N extends Ordinal> 
    extends 
        NVectorSpace<N, Double, Double> 
    implements 
        InnerProductSpace<HomTuple<N, Double>, Double>, 
        FiniteVectorSpace<HomTuple<N, Double>, Double> {

    protected VecD() {
        super(DoubleField.INSTANCE);
    }
}
