package simulation.parameters;

import util.properties.EnableListener;
import util.properties.EnableableValue;
import util.properties.UpdatableValue;
import util.properties.UpdateListener;

public abstract class Parameter<T> implements NamedParameter, UpdatableValue<T>, EnableableValue {
    
    /**
     * @return whether this parameter will accept value changes
     */
    @Override
    public abstract boolean isEnabled();

    /**
     * @return the value of this parameter
     */
    @Override
    public abstract T getValue();
    
    /**
     * @return the minimum allowable parameter value
     */
    public abstract T minAllowableValue();
    
    /**
     * @return the maximum allowable parameter value
     */
    public abstract T maxAllowableValue();

    /**
     * @return the default value for this parameter
     */
    public abstract T defaultValue();

    /**
     * Sets this parameter's value to the supplied value.
     * 
     * @param parameter the value to give to this parameter
     */
    @Override
    public abstract void setValue(final T parameter);

    /**
     * Sets whether or not this parameter should allow itself to be changed.
     * 
     * @param enabled 
     */
    @Override
    public abstract void setEnabled(final boolean enabled);

    /**
     * Adds a listener that will be updated on a call to {@link Parameter#setValue(Object)}.
     * 
     * @param listener the listener to add
     */
    @Override
    public abstract void addUpdateListener(final UpdateListener<T> listener);

    /**
     * Adds a listener that will be updated on a call to {@link Parameter#setEnabled(boolean)} that
     * changes the enabled state.
     */
    @Override
    public abstract void addEnableListener(final EnableListener listener);
}
