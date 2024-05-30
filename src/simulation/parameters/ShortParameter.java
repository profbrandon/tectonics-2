package simulation.parameters;

public class ShortParameter extends BasicParameter<Short> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public ShortParameter(final String name, final String description) {
        this(name, description, (short) 0x0, (short) 0x0, (short) 0xFF);
    }

    public ShortParameter(
        final String name,
        final String description,
        final Short defaultValue,
        final Short minValue,
        final Short maxValue) {

        super(name, description, defaultValue, minValue, maxValue);
    }
}
