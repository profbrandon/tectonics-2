package util.math.instances.doubles.covectors;

import util.counting.Cardinals.One;
import util.math.instances.doubles.vectors.Vec1D;

/**
 * Class to represent 1-dimensional covectors (of {@link Vec1D}).
 */
public class CoVec1D extends CoVecD<One> {
    
    public static final CoVecD<One> INSTANCE = new CoVec1D();

    private CoVec1D() {
        super(Vec1D.INSTANCE);
    }
}
