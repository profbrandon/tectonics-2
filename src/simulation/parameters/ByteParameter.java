package simulation.parameters;

public class ByteParameter extends BasicParameter<Byte> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public ByteParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, (byte) 0x0, (byte) 0x0, (byte) 0xFF);
    }
}
