package util.math.instances.doubles;

import util.counting.Ordinals.Two;

public class Linear2x2D extends LinearD<Two, Two> {
    
    public static LinearD<Two, Two> INSTANCE = new Linear2x2D();

    public Linear2x2D() {
        super(Vec2D.INSTANCE);
    }
}
