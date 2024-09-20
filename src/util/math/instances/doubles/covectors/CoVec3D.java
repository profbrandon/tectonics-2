package util.math.instances.doubles.covectors;

import java.util.List;

import util.counting.Cardinals.Three;
import util.counting.Ordinal;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.Vec3D;

/**
 * Class to represent 3-dimensional covectors (of {@link Vec3D}).
 */
public class CoVec3D
    extends 
        CoVecD<Three> {
    
    public static final CoVecD<Three> INSTANCE = new CoVec3D();

    /**
     * The zero 2-dimensional double-valued covector
     */
    public static final Exp<HomTuple<Three, Double>, Double> ZERO = INSTANCE.zero();

    /**
     * The unit 2-dimensional "x" double-valued covector.
     */
    public static final Exp<HomTuple<Three, Double>, Double>
        UNIT_X = Exp.asExponential(tuple -> tuple.at(Ordinal.ZERO_3));

    /**
     * The unit 2-dimensional "y" double-valued covector.
     */
    public static final Exp<HomTuple<Three, Double>, Double>
        UNIT_Y = Exp.asExponential(tuple -> tuple.at(Ordinal.ONE_3));

    /**
     * The unit 3-dimensional "z" double-valued covector.
     */
    public static final Exp<HomTuple<Three, Double>, Double>
        UNIT_Z = Exp.asExponential(tuple -> tuple.at(Ordinal.TWO_3));

    private CoVec3D() {
        super(Vec3D.INSTANCE);
    }

    /**
     * Creates a covector with the given components.
     * 
     * @param x the "x" component
     * @param y the "y" component
     * @param z the "z" component
     * @return the corresponding dual vector
     */
    public static Exp<HomTuple<Three, Double>, Double> covector(final double x, final double y, final double z) {
        return INSTANCE.sumAll(List.of(
            INSTANCE.scale(UNIT_X, x), 
            INSTANCE.scale(UNIT_Y, y),
            INSTANCE.scale(UNIT_Z, z)));
    }
}
