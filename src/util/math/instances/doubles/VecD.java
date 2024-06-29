package util.math.instances.doubles;

import util.counting.Ordinal;
import util.math.vectorspaces.NVectorSpace;

public abstract class VecD<O extends Ordinal> extends NVectorSpace<O, Double, Double> {
    protected VecD() {
        super(DoubleField.INSTANCE);
    }
}
