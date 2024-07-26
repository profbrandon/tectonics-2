package util.math.vectorspaces.finite;

import java.util.Collection;
import java.util.List;

import util.counting.Cardinal;
import util.counting.Combinatorics;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.vectorspaces.MultilinearNFormSpace;

public class FiniteMultilinearNFormSpace<N extends Cardinal, V, K>
    extends
        MultilinearNFormSpace<N, V, K>
    implements
        FiniteVectorSpace<Exp<HomTuple<N, V>, K>, K> {

    private final FiniteDualSpace<V, K> FINITE_DUAL;

    public FiniteMultilinearNFormSpace(
        final Collection<Ordinal<N>> enumerated,
        final FiniteDualSpace<V, K> finiteDualSpace) {

        super(enumerated, finiteDualSpace);

        this.FINITE_DUAL = finiteDualSpace;
    }

    public HomTuple<N, V> dualDualAsVector(final Exp<HomTuple<N, Exp<V, K>>, K> dualDual) {
        final List<HomTuple<N, Exp<V, K>>> multiDualBasis = Combinatorics.nProduct(
            List.copyOf(underlyingOrdinalSet()), 
            underlyingDualSpace().basis());

        return multiDualBasis
            .stream()
            .map(tuple -> 
                tuple
                    .mapAll(underlyingDualSpace()::dualAsVector)
                    .mapAll(b -> underlyingDomainSpace().scale(b, dualDual.apply(tuple))))
            .reduce(
                HomTuple.all(underlyingDomainSpace().zero()), 
                (tuple1, tuple2) -> 
                    new HomTuple<>(ord -> underlyingDomainSpace().sum(tuple1.at(ord), tuple2.at(ord))));
    }

    public Exp<HomTuple<N, V>, K> vectorAsDual(final HomTuple<N, V> vector) {
        final List<HomTuple<N, Exp<V, K>>> multiDualBasis = Combinatorics.nProduct(
            List.copyOf(underlyingOrdinalSet()), 
            underlyingDualSpace().basis());

        return sumAll(
            multiDualBasis
                .stream()
                .map(tuple -> 
                    scale(fromLinear(tuple), fromLinear(tuple).apply(vector)))
                .toList());
    }

    public HomTuple<N, V> dualAsVector(final Exp<HomTuple<N, V>, K> dual) {
        final List<HomTuple<N, V>> multiBasis = Combinatorics.nProduct(
            List.copyOf(underlyingOrdinalSet()), 
            underlyingDomainSpace().basis());

        return multiBasis
            .stream()
            .map(b -> b.mapAll(v -> underlyingDomainSpace().scale(v, dual.apply(b))))
            .reduce(
                HomTuple.all(underlyingDomainSpace().zero()),
                (tuple1, tuple2) -> 
                    new HomTuple<>(ord -> underlyingDomainSpace().sum(tuple1.at(ord), tuple2.at(ord))));
    }

    public HomTuple<N, Exp<V, K>> toLinear(final Exp<HomTuple<N, V>, K> dual) {
        return dualAsVector(dual).mapAll(underlyingDualSpace()::vectorAsDual);
    }

    @Override
    public FiniteVectorSpace<V, K> underlyingDomainSpace() {
        return this.FINITE_DUAL.domainVectorSpace();
    }

    @Override
    public FiniteDualSpace<V, K> underlyingDualSpace() {
        return FINITE_DUAL;
    }

    @Override
    public List<Exp<HomTuple<N, V>, K>> basis() {
        return 
            Combinatorics.nProduct(
                    List.copyOf(underlyingOrdinalSet()), 
                    underlyingDualSpace().basis())
                .stream()
                .map(this::fromLinear)
                .toList();
    }

    @Override
    public List<Prod<K, Exp<HomTuple<N, V>, K>>> decompose(Exp<HomTuple<N, V>, K> v) {
        return
            Combinatorics.nProduct(
                    List.copyOf(underlyingOrdinalSet()),
                    underlyingDualSpace().domainVectorSpace().basis())
                .stream()
                .map(
                    baseTuple -> 
                        Prod.pair(
                            v.apply(baseTuple), 
                            fromLinear(baseTuple.mapAll(underlyingDualSpace()::vectorAsDual))))
                .toList();
    }
}
