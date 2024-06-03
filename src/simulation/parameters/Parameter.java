package simulation.parameters;

public abstract class Parameter<T> implements NamedParameter {
    /**
     * @return the value of this parameter
     */
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
    public abstract void setValue(final T parameter);
}
