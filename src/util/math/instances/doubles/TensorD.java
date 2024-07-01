package util.math.instances.doubles;

import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.FiniteVectorSpace;
import util.math.vectorspaces.PQTensorSpace;

public abstract class TensorD<P extends Ordinal, Q extends Ordinal>
    extends PQTensorSpace<Double, Double, P, Q>
    implements FiniteVectorSpace<Prod<HomTuple<P, Double>, HomTuple<Q, Exp<Double, Double>>>, Double> {

    protected TensorD() {
        super(DoubleField.INSTANCE, DoubleField.INSTANCE);
    }
}
