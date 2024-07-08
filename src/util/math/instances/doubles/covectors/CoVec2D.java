package util.math.instances.doubles.covectors;

import util.counting.Cardinals.Two;
import util.math.instances.doubles.vectors.Vec2D;

public class CoVec2D extends CoVecD<Two> {
    
    public static final CoVecD<Two> INSTANCE = new CoVec2D();

    private CoVec2D() {
        super(Vec2D.INSTANCE);
    }
}
