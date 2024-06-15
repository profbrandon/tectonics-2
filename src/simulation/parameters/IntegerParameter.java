package simulation.parameters;

public class IntegerParameter extends BasicParameter<Integer> {
    /**
     * Constructor that automatically supplies a default value of 0, a minimum of {@link Integer#MIN_VALUE}, and a maximum of {@link Integer#MAX_VALUE}.
     * 
     * @param name the name of the parameter
     * @param abbreviation the abbreviation for the parameter
     * @param description the description of the parameter
     */
    public IntegerParameter(final String name, final String abbreviation, final String description) {
        super(name, abbreviation, description, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerParameter(
        final String name,
        final String abbreviation,
        final String description,
        final int defaultValue,
        final int minValue,
        final int maxValue) {

        super(name, abbreviation, description, defaultValue, minValue, maxValue);
    }
}
