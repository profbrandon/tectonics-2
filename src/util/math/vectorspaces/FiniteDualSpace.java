package util.math.vectorspaces;

import java.util.List;

import util.data.algebraic.Exp;
import util.data.algebraic.Prod;
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

    @Override
    public boolean equiv(final Exp<V, K> a1, final Exp<V, K> a2) {
        final List<K> components1 = decompose(a1).stream().map(Prod::first).toList();
        final List<K> components2 = decompose(a2).stream().map(Prod::first).toList();

        if (components1.size() != components2.size()) {
            return false;
        }

        for (int i = 0; i < components1.size(); ++i) {
            if (!underlyingField().equiv(components1.get(i), components2.get(i))) return false;
        }

        return true;
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
