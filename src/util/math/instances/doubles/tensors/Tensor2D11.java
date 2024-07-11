package util.math.instances.doubles.tensors;

import java.util.List;
import java.util.stream.Stream;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Prev;
import util.counting.Cardinals.One;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.data.algebraic.Sum;
import util.math.instances.doubles.covectors.CoVec2D;

public class Tensor2D11 extends TensorD<Two, One, One> {
    
    public static final TensorD<Two, One, One> INSTANCE = new Tensor2D11();

    private Tensor2D11() {
        super(CoVec2D.INSTANCE);
    }

    public Exp<HomTuple<Two, Double>, HomTuple<Two, Double>> asLinearMap(
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor) {

        return Exp.asExponential(
            v -> underlyingDualSpace()
                .dualDualAsVector(Exp.asExponential(
                    dv -> evaluate(tensor, HomTuple.tuple(dv), HomTuple.tuple(v)))));
    }

    @Override
    public Sum<Double, Prod<HomTuple<Prev<One>, HomTuple<Two, Double>>, HomTuple<Prev<One>, Exp<HomTuple<Two, Double>, Double>>>> contract(
        final Ordinal<One> index1,
        final Ordinal<One> index2,
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor) {
        
        Preconditions.throwIfNull(index1, "index1");
        Preconditions.throwIfNull(index2, "index2");
        Preconditions.throwIfNull(tensor, "tensor");

        return Sum.left(tensor.destroy(v -> dv -> dv.at(index2).apply(v.at(index1))));
    }

    @Override
    public List<Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>> basis() {
        return Stream.concat(
                underlyingVectorSpace()
                    .basis()
                    .stream()
                    .map(b -> Prod.pair(HomTuple.tuple(b), HomTuple.tuple(underlyingDualSpace().zero()))),
                underlyingDualSpace()
                    .basis()
                    .stream()
                    .map(b -> Prod.pair(HomTuple.tuple(underlyingVectorSpace().zero()), HomTuple.tuple(b))))
            .toList();
    }

    @Override
    public List<Prod<Double, Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>>>> decompose(
        final Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor) {
        
        return basis().stream().map(b -> Prod.pair(evaluate(tensor, b.second(), b.first()), b)).toList();
    }

    @Override
    public Double evaluate(
            Prod<HomTuple<One, HomTuple<Two, Double>>, HomTuple<One, Exp<HomTuple<Two, Double>, Double>>> tensor,
            HomTuple<One, Exp<HomTuple<Two, Double>, Double>> dualVectors,
            HomTuple<One, HomTuple<Two, Double>> vectors) {
        
        return evaluate(Ordinal.ONE_SET, Ordinal.ONE_SET, tensor, dualVectors, vectors);
    }
}
