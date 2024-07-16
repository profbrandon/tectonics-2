package util.math.instances.doubles.covectors;

import util.counting.Ordinal;
import util.counting.Cardinals.Two;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.Vec2D;

/**
 * Class to represent 2-dimensional covectors (of {@link Vec2D}).
 */
public class CoVec2D 
    extends 
        CoVecD<Two> {
    
    public static final CoVecD<Two> INSTANCE = new CoVec2D();

    /**
     * The zero 2-dimensional double-valued covector
     */
    public static final Exp<HomTuple<Two, Double>, Double> ZERO = INSTANCE.zero();

    /**
     * The unit 2-dimensional "x" double-valued covector.
     */
    public static final Exp<HomTuple<Two, Double>, Double>
        UNIT_X = Exp.asExponential(tuple -> tuple.at(Ordinal.ZERO_2));

    /**
     * The unit 2-dimensional "y" double-valued covector.
     */
    public static final Exp<HomTuple<Two, Double>, Double>
        UNIT_Y = Exp.asExponential(tuple -> tuple.at(Ordinal.ONE_2));

    private CoVec2D() {
        super(Vec2D.INSTANCE);
    }
}
