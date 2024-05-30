package simulation.parameters;

public class DoubleParameter extends BasicParameter<Double> {
    /**
     * Constructor that automatically supplies a default value of 0.0, a minimum of {@link Double#MIN_VALUE}, and a maximum of {@link Double#MIN_VALUE}.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public DoubleParameter(final String name, final String description) {
        this(name, description, 0.0, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public DoubleParameter(
        final String name,
        final String description,
        final Double defaultValue,
        final Double minValue,
        final Double maxValue) {

        super(name, description, defaultValue, minValue, maxValue);
    }
}
