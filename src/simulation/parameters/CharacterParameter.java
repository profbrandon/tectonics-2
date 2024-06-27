package simulation.parameters;

public class CharacterParameter extends BasicParameter<Character> {
    /**
     * Constructor that automatically supplies a default value of 0x0, a minimum of 0x0, and a maximum of 0xFF.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public CharacterParameter(final String name, final String abbreviation, final String description) {
        this(name, abbreviation, description, '\0');
    }

    public CharacterParameter(
        final String name,
        final String abbreviation,
        final String description,
        final Character defaultValue) {
    
        super(name, abbreviation, description, defaultValue, '\0', '\0');
    }
}
