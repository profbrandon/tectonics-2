package simulation.parameters;

public class DoubleParameter extends BasicParameter<Double> {
    /**
     * Constructor that automatically supplies a default value of 0.0, a minimum of {@link Double#MIN_VALUE}, and a maximum of {@link Double#MIN_VALUE}.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public DoubleParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, 0.0, Double.MIN_VALUE, Double.MAX_VALUE);
    }
}
