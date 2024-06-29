package util.math;

public interface Group<G> extends Monoid<G> {

    public G neg(final G g);
}
