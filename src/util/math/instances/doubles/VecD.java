package util.math.instances.doubles;

import util.counting.Ordinal;
import util.data.algebraic.HomTuple;
import util.math.InnerProductSpace;
import util.math.vectorspaces.NVectorSpace;

public abstract class VecD<O extends Ordinal> extends NVectorSpace<O, Double, Double> implements InnerProductSpace<HomTuple<O, Double>, Double> {
    protected VecD() {
        super(DoubleField.INSTANCE);
    }
}
