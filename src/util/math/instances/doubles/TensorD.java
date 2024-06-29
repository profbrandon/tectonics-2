package util.math.instances.doubles;

import util.counting.Ordinal;
import util.math.vectorspaces.PQTensorSpace;

public abstract class TensorD<P extends Ordinal, Q extends Ordinal> extends PQTensorSpace<Double, Double, P, Q> {
    protected TensorD() {
        super(DoubleField.INSTANCE, DoubleField.INSTANCE);
    }
}
