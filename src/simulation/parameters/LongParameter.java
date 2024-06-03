package simulation.parameters;

public class LongParameter extends BasicParameter<Long> {
    /**
     * Constructor that automatically supplies a default value of 0, a minimum of {@link Integer#MIN_VALUE}, and a maximum of {@link Integer#MAX_VALUE}.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public LongParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, 0l, Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
