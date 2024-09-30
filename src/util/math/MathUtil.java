package util.math;

import java.util.function.Function;

import util.Functional;
import util.counting.Cardinal;
import util.data.algebraic.HomTuple;
import util.data.algebraic.Maybe;
import util.data.algebraic.Prod;
import util.math.vectorspaces.NVectorSpace;

public final class MathUtil {
    
    public static double interpolate(final double x0, final double x1, final double t) {
        return (x1 - x0) * t + x0;
    }

    public static float interpolate(final float x0, final float x1, final float t) {
        return (x1 - x0) * t + x0;
    }

    public static <N extends Cardinal, K> HomTuple<N, K> interpolate(
        final NVectorSpace<N, K, K> vspace, 
        final HomTuple<N, K> x0, 
        final HomTuple<N, K> x1, 
        final K t) {

        return vspace.sum(vspace.scale(vspace.subVec(x1, x0), t), x0);
    }

    public static Function<Prod<Float, Float>, Float> interpolate(final float[][] array) {
        return pair -> pair.destroy(
            i -> j ->
                Functional.let(i.intValue(), (Integer i0) ->
                Functional.let(j.intValue(), (Integer j0) -> 
                Functional.let(i - i0, (Float t0) ->
                Functional.let(j - j0, (Float t1) ->
                interpolate(
                    interpolate(array[i0][j0],     array[i0 + 1][j0],     t1),
                    interpolate(array[i0][j0 + 1], array[i0 + 1][j0 + 1], t1),
                    t0))))));
    }

    /**
     * Turns an unbounded floating point number into a bounded one on the interval (-1, 1). 
     * Close to zero, this function is approximately {@code squeeze(x) = x}.
     * 
     * @param value the value to squeeze
     * @return the squeezed value
     */
    public static float squeeze(final float value) {
        return 1.0f - 2.0f / ((float)Math.pow(2.0f, 3.0f * value) + 1f);
    }

    /**
     * Undoes {@link MathUtil#squeeze(float)} if possible.
     * 
     * @param value the value to expand
     * @return the expanded value
     */
    public static Maybe<Float> unsqueeze(final float value) {
        if (value >= 1.0f || value <= -1.0) return Maybe.nothing();
        else return 
            Maybe.just(
                Functional.let(2.0f / (1.0f - value), (Float z) ->
                (float)(Math.log(z) / Math.log(2) / 3.0f)));
    }
}
