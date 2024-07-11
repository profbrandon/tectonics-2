package util.math.vectorspaces;

import java.util.List;

import util.data.algebraic.Exp;
import util.math.Field;

public abstract class FiniteDualSpace<V, K> 
    extends FieldValuedSpace<V, K> 
    implements FiniteVectorSpace<Exp<V, K>, K> {

    private final FiniteVectorSpace<V, K> UNDERLYING_SPACE;

    public FiniteDualSpace(final FiniteVectorSpace<V, K> underlyingSpace) {
        super(underlyingSpace);
        
        this.UNDERLYING_SPACE = underlyingSpace;
    }

    public FiniteVectorSpace<V, K> underlyingFiniteVectorSpace() {
        return this.UNDERLYING_SPACE;
    }

    @Override
    public Field<K> underlyingField() {
        return UNDERLYING_SPACE.underlyingField();
    }
    
    @Override
    public List<Exp<V, K>> basis() {
        return UNDERLYING_SPACE.basis()
            .stream()
            .map(
                this::covectorFromBasisVector)
            .toList();
    }

    public V dualAsVector(final Exp<V, K> dualvector) {
        return underlyingFiniteVectorSpace()
            .sumAll(
                underlyingFiniteVectorSpace()
                    .basis()
                    .stream()
                    .map(b -> underlyingFiniteVectorSpace().scale(b, dualvector.apply(b)))
                    .toList());
    }

    public V dualDualAsVector(final Exp<Exp<V, K>, K> ddvector) {
        return underlyingFiniteVectorSpace()
            .sumAll(
                basis()
                    .stream()
                    .map(b -> underlyingFiniteVectorSpace().scale(dualAsVector(b), ddvector.apply(b))).toList());
    }

    protected Exp<V, K> covectorFromBasisVector(final V b) {
        return Exp.<V, K>asExponential(
            v -> UNDERLYING_SPACE.decompose(v)
                .stream()
                .map(pair -> pair.destroy(
                    vi -> 
                        bi -> 
                            bi == b ? super.underlyingField().zero() : vi))
                .reduce(super.underlyingField()::sum)
                .get());
    }
}
