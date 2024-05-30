package simulation.parameters;

public class StringParameter extends BasicParameter<String> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public StringParameter(final String name, final String description) {
        this(name, description, "");
    }

    public StringParameter(
        final String name,
        final String description,
        final String defaultValue) {
    
        super(name, description, defaultValue, "", "");
    }

    @Override
    public void setValue(final String parameter) {
        assert parameter != null;

        this.value = parameter;
    }
}
