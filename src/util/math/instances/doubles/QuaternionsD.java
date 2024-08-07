package util.math.instances.doubles;

import util.data.algebraic.Prod;
import util.math.vectorspaces.Quaternions;

public class QuaternionsD
    extends
        Quaternions<Double> {
    
    public static final Quaternions<Double> INSTANCE = new QuaternionsD();

    private QuaternionsD() {
        super(DoubleField.INSTANCE);
    }

    /**
     * Computes the norm, i.e., the distance to the origin.
     * 
     * @param q the quaternion
     * @return the distance to the origin
     */
    public static double norm(final Prod<Prod<Double, Double>, Prod<Double, Double>> q) {
        return Math.sqrt(INSTANCE.magnitude2(q));
    }
}
