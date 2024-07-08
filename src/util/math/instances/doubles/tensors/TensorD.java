package util.math.instances.doubles.tensors;

import java.util.List;

import util.Preconditions;
import util.counting.Cardinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.instances.doubles.DoubleField;
import util.math.instances.doubles.covectors.CoVecD;
import util.math.vectorspaces.FiniteDualSpace;
import util.math.vectorspaces.FiniteVectorSpace;
import util.math.vectorspaces.PQTensorSpace;

public abstract class TensorD<N extends Cardinal, P extends Cardinal, Q extends Cardinal>
    extends 
        PQTensorSpace<HomTuple<N, Double>, Double, P, Q>
    implements 
        FiniteVectorSpace<Prod<HomTuple<P, HomTuple<N, Double>>, HomTuple<Q, Exp<HomTuple<N, Double>, Double>>>, Double> {

    private final FiniteVectorSpace<HomTuple<N, Double>, Double> FINITE_VECTOR_SPACE;
    private final CoVecD<N> FINITE_DUAL_SPACE;

    protected TensorD(final CoVecD<N> underlyingDualSpace) {
        super(underlyingDualSpace, DoubleField.INSTANCE);

        this.FINITE_VECTOR_SPACE = underlyingDualSpace.underlyingFiniteVectorSpace();
        this.FINITE_DUAL_SPACE = underlyingDualSpace;
    }

    @Override
    public FiniteVectorSpace<HomTuple<N, Double>, Double> underlyingVectorSpace() {
        return this.FINITE_VECTOR_SPACE;
    }

    @Override
    public FiniteDualSpace<HomTuple<N, Double>, Double> underlyingDualSpace() {
        return FINITE_DUAL_SPACE;
    }

    @Override
    public boolean equiv(
        final Prod<HomTuple<P, HomTuple<N, Double>>, HomTuple<Q, Exp<HomTuple<N, Double>, Double>>> v1,
        final Prod<HomTuple<P, HomTuple<N, Double>>, HomTuple<Q, Exp<HomTuple<N, Double>, Double>>> v2) {
        
        Preconditions.throwIfNull(v1, "v1");
        Preconditions.throwIfNull(v2, "v2");

        final List<Double> components1 = decompose(v1).stream().map(pair -> pair.first()).toList();
        final List<Double> components2 = decompose(v2).stream().map(pair -> pair.first()).toList();

        for (int i = 0; i < basis().size(); ++i) {
            if (!underlyingField().equiv(components1.get(i), components2.get(i))) return false;
        }

        return true;
    }
}