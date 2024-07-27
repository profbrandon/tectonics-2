package util.math.fields;

import util.math.Field;

/**
 * Interface to represent a subfield relationship between this {@link Field} and another.
 */
public interface SubField<F1, F2, X extends Field<F2>> extends Field<F1> {
    
    public F2 embedField(final F1 f1);
}
