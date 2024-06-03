package simulation.parameters;

public interface NamedParameter {
    /**
     * @return the name of this parameter
     */
    public String getName();

    /**
     * @return the abbreviation used to refer to this parameter
     */
    public String getAbbreviation();
    
    /**
     * @return the description of this parameter
     */
    public String getDescription();
}
