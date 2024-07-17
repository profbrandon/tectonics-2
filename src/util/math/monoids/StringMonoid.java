package util.math.monoids;

import util.Preconditions;
import util.math.Monoid;

public class StringMonoid
    implements
        Monoid<String> {

    @Override
    public boolean equiv(final String a1, final String a2) {
        Preconditions.throwIfNull(a1, "a1");
        Preconditions.throwIfNull(a2, "a2");
        return a1.equals(a2);
    }

    @Override
    public String zero() {
        return "";
    }

    @Override
    public String sum(final String m1, final String m2) {
        Preconditions.throwIfNull(m1, "m1");
        Preconditions.throwIfNull(m2, "m2");
        return m1 + m2;
    }
}
