package util.math.instances.doubles.tensors;

import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.vectorspaces.FiniteVectorSpace;
import util.math.vectorspaces.PQTensorSpace;

public abstract class TensorD<P extends Cardinal, Q extends Cardinal>
    extends PQTensorSpace<Double, Double, P, Q>
    implements FiniteVectorSpace<Prod<HomTuple<P, Double>, HomTuple<Q, Exp<Double, Double>>>, Double> {

    protected TensorD() {
        super(DoubleField.INSTANCE, DoubleField.INSTANCE);
    }
}
