package util.data.algebraic;

public final class Unit {
    private static Unit singletonInstance = new Unit();
 
    private Unit() {

    }

    public final <A> A destroy(final A value) {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            return true;
        } else {
            return false;
        }
    }

    public final static Unit unit() {
        return singletonInstance;
    }
}
