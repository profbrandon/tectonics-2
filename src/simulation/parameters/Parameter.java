package simulation.parameters;

public abstract class Parameter<T> {
    /**
     * @return the name of this parameter
     */
    abstract String getName();
    
    /**
     * @return the description of this parameter
     */
    abstract String getDescription();
    
    /**
     * @return the value of this parameter
     */
    abstract T getValue();
    
    /**
     * @return the minimum allowable parameter value
     */
    abstract T minAllowableValue();
    
    /**
     * @return the maximum allowable parameter value
     */
    abstract T maxAllowableValue();

    /**
     * @return the default value for this parameter
     */
    abstract T defaultValue();

    /**
     * Sets this parameter's value to the supplied value.
     * 
     * @param parameter the value to give to this parameter
     */
    abstract void setValue(final T parameter);
}
