package simulation.parameters;

public class BooleanParameter extends BasicParameter<Boolean> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public BooleanParameter(final String name, final String description) {
        this(name, description, false, false, true);
    }

    public BooleanParameter(
        final String name,
        final String description,
        final Boolean defaultValue,
        final Boolean minValue,
        final Boolean maxValue) {

        super(name, description, defaultValue, minValue, maxValue);
    }
}
