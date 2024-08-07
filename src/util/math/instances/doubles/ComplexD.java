package util.math.instances.doubles;

import util.data.algebraic.Prod;
import util.math.fields.Complex;

/**
 * Class to represent the complex number space over the field of doubles.
 */
public class ComplexD 
    extends 
        Complex<Double> {

    public static final Complex<Double> INSTANCE = new ComplexD();

    private ComplexD() {
        super(DoubleField.INSTANCE);
    }

    /**
     * Computes the norm, i.e., the distance to the origin.
     * 
     * @param c the complex number
     * @return the distance to the origin
     */
    public static double norm(final Prod<Double, Double> c) {
        return Math.sqrt(INSTANCE.magnitude2(c));
    }
}
