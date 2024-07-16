package util.math.instances.doubles.covectors;

import util.counting.Ordinal;
import util.counting.Cardinals.One;
import util.data.algebraic.Exp;
import util.data.algebraic.HomTuple;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent 1-dimensional covectors (of {@link Vec1D}).
 */
public class CoVec1D 
    extends 
        CoVecD<One> {
    
    public static final CoVecD<One> INSTANCE = new CoVec1D();

    /**
     * The zero 1-dimensional double-valued covector.
     */
    public static final Exp<HomTuple<One, Double>, Double> ZERO = INSTANCE.zero();

    /**
     * The unit 1-dimensional double-valued covector, i.e., the identity linear transformation.
     */
    public static final Exp<HomTuple<One, Double>, Double>
        UNIT = Exp.asExponential(tuple -> tuple.at(Ordinal.ZERO_1));

    private CoVec1D() {
        super(Vec1D.INSTANCE);
    }
}
