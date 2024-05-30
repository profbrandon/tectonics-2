package simulation.parameters;

public class IntegerParameter extends BasicParameter<Integer> {
    /**
     * Constructor that automatically supplies a default value of 0, a minimum of {@link Integer#MIN_VALUE}, and a maximum of {@link Integer#MAX_VALUE}.
     * 
     * @param name the name of the parameter
     * @param description the description of the parameter
     */
    public IntegerParameter(final String name, final String description) {
        this(name, description, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerParameter(
        final String name,
        final String description,
        final Integer defaultValue,
        final Integer minValue,
        final Integer maxValue) {

        super(name, description, defaultValue, minValue, maxValue);
    }
}
