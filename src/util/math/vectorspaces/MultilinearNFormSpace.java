package util.math.vectorspaces;

import java.util.Collection;

import util.counting.Cardinal;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;

public abstract class MultilinearNFormSpace<N extends Cardinal, V, K> 
    extends MultilinearNMapSpace<N, V, K, K> {

    private final DualSpace<V, K> DUAL_SPACE;

    public MultilinearNFormSpace(
        final Collection<Ordinal<N>> enumerated, 
        final DualSpace<V, K> underlyingDual) {

        super(enumerated, underlyingDual.domainVectorSpace(), underlyingDual.underlyingField());
    
        this.DUAL_SPACE = underlyingDual;
    }

    public DualSpace<V, K> underlyingDualSpace() {
        return DUAL_SPACE;
    }

    public Exp<HomTuple<N, V>, K> fromLinear(final HomTuple<N, Exp<V, K>> linearMaps) {
        return Exp.asExponential(
            tuple -> 
                linearMaps.eliminate(
                    underlyingOrdinalSet(),
                    underlyingField().unit(),
                    ord -> pair -> pair.destroy(
                        k -> linear ->
                            underlyingField().mult(k, linear.apply(tuple.at(ord))))));
    }

    public Exp<HomTuple<N, Exp<V, K>>, K> vectorAsDualDual(final HomTuple<N, V> vector) {
        return Exp.asExponential(
            tuple ->
                vector.eliminate(
                    underlyingOrdinalSet(), 
                    underlyingField().unit(), 
                    ord -> pair -> pair.destroy(
                        k -> v -> underlyingField().mult(k, tuple.at(ord).apply(v)))));
    }
}
