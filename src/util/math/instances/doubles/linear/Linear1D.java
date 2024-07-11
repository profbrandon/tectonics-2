package util.math.instances.doubles.linear;

import java.util.List;

import util.Preconditions;
import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Prod;
import util.math.Ring;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent the 1-dimensional linear endomorphisms on {@link Vec2D}. Note that this
 * is, in essence, isomorphic to {@link DoubleField} due to the restriction of linearity.
 */
public class Linear1D extends LinearD<One, One> implements Ring<Exp<HomTuple<One, Double>, HomTuple<One, Double>>> {

    public static final LinearD<One, One> INSTANCE = new Linear1D();

    /**
     * The unit value.
     */
    public static final Exp<HomTuple<One, Double>, HomTuple<One, Double>> UNIT = 
        Exp.constant(HomTuple.tuple(INSTANCE.underlyingField().unit()));
    
    private Linear1D() {
        super(Vec1D.INSTANCE, Vec1D.INSTANCE);
    }

    @Override
    public List<Exp<HomTuple<One, Double>, HomTuple<One, Double>>> basis() {
        return List.of(UNIT);
    }

    @Override
    public List<Prod<Double, Exp<HomTuple<One, Double>, HomTuple<One, Double>>>> decompose(
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> linear) {

        Preconditions.throwIfNull(linear, "linear");
        return List.of(Prod.pair(linear.apply(HomTuple.tuple(underlyingField().unit())).at(Ordinal.ZERO_1), UNIT));
    }

    @Override
    public Exp<HomTuple<One, Double>, HomTuple<One, Double>> unit() {
        return UNIT;
    }

    @Override
    public Exp<HomTuple<One, Double>, HomTuple<One, Double>> mult(
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> linear1,
        final Exp<HomTuple<One, Double>, HomTuple<One, Double>> linear2) {
        
        Preconditions.throwIfNull(linear1, "linear1");
        Preconditions.throwIfNull(linear2, "linear2");
        return linear1.after(linear2);
    }
}
