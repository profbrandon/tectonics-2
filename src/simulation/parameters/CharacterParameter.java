package simulation.parameters;

public class CharacterParameter extends BasicParameter<Character> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public CharacterParameter(final String name, final String description) {
        this(name, description, '\0');
    }

    public CharacterParameter(
        final String name,
        final String description,
        final Character defaultValue) {
    
        super(name, description, defaultValue, '\0', '\0');
    }

    @Override
    public void setValue(final Character parameter) {
        assert parameter != null;

        this.value = parameter;
    }
}
