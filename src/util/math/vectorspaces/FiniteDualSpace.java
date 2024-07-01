package util.math.vectorspaces;

import java.util.List;

import util.data.algebraic.Exp;
import util.math.Field;
import util.math.FiniteVectorSpace;

public abstract class FiniteDualSpace<V, K> extends FieldValuedSpace<V, K> implements FiniteVectorSpace<Exp<V, K>, K> {

    protected final FiniteVectorSpace<V, K> UNDERLYING_SPACE;

    public FiniteDualSpace(final FiniteVectorSpace<V, K> underlyingSpace, final Field<K> underylingField) {
        super(underylingField);
        this.UNDERLYING_SPACE = underlyingSpace;
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
                                    bi == b ? UNDERLYING_F.zero() : vi))
                        .reduce(UNDERLYING_F::sum)
                        .get()))
            .toList();
    }
}
