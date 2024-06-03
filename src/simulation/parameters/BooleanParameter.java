package simulation.parameters;

public class BooleanParameter extends BasicParameter<Boolean> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public BooleanParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, false, false, true);
    }
}
