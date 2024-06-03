package simulation.parameters;

public class FloatParameter extends BasicParameter<Float> {
    /**
     * Constructor that automatically supplies a default value of 0f, a minimum of {@link Float#MIN_VALUE}, and a maximum of {@link Float#MAX_VALUE}.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public FloatParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, 0f, Float.MIN_VALUE, Float.MAX_VALUE);
    }
}
