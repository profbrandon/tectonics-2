package simulation.parameters;

public class ByteParameter extends BasicParameter<Byte> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public ByteParameter(final String name, final String description) {
        this(name, description, (byte) 0x0, (byte) 0x0, (byte) 0xFF);
    }

    public ByteParameter(
        final String name,
        final String description,
        final Byte defaultValue,
        final Byte minValue,
        final Byte maxValue) {

        super(name, description, defaultValue, minValue, maxValue);
    }
}
