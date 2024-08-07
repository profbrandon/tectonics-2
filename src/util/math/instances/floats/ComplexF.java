package util.math.instances.floats;

import util.data.algebraic.Prod;
import util.math.fields.Complex;
import util.math.fields.SubField;
import util.math.instances.doubles.ComplexD;

public class ComplexF
    extends
        Complex<Float>
    implements
        SubField<Prod<Float, Float>, Prod<Double, Double>, ComplexD> {

    public ComplexF() {
        super(FloatField.INSTANCE);
    }

    @Override
    public Prod<Double, Double> embedField(Prod<Float, Float> f1) {
        return Prod.pair((double) f1.first(), (double) f1.second());
    }
}
