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
                b -> Exp.<V, K>asExponential(
                    v -> UNDERLYING_SPACE.decompose(v)
                        .stream()
                        .map(pair -> pair.destroy(
                            vi -> 
                                bi -> 
                                    bi == b ? super.underlyingField().zero() : vi))
                        .reduce(super.underlyingField()::sum)
                        .get()))
            .toList();
    }
}
