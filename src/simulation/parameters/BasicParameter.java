package simulation.parameters;

public class BasicParameter<T extends Comparable<T>> extends Parameter<T> { 
    private final String NAME;
    private final String ABBREVIATION;
    private final String DESCRIPTION;
    
    private final T DEFAULT_VALUE;
    private final T MIN_VALUE;
    private final T MAX_VALUE;

    protected T value;

    public BasicParameter(
        final String name,
        final String abbreviation,
        final String description,
        final T defaultValue,
        final T minValue,
        final T maxValue) {

        assert name         != null;
        assert abbreviation != null;
        assert description  != null;
        assert defaultValue != null;
        assert minValue     != null;
        assert maxValue     != null;

        this.NAME          = name;
        this.ABBREVIATION  = abbreviation;
        this.DESCRIPTION   = description;
        this.DEFAULT_VALUE = defaultValue;
        this.MIN_VALUE     = minValue;
        this.MAX_VALUE     = maxValue;

        this.value = this.defaultValue();
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public String getAbbreviation() {
        return this.ABBREVIATION;
    }

    @Override
    public String getDescription() {
        return this.DESCRIPTION;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public T minAllowableValue() {
        return this.MIN_VALUE;
    }

    @Override
    public T maxAllowableValue() {
        return this.MAX_VALUE;
    }

    @Override
    public T defaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public void setValue(final T parameter) {
        assert parameter != null;

        if (parameter.compareTo(this.MIN_VALUE) >= 0 && parameter.compareTo(this.MAX_VALUE) <= 0) {
            this.value = parameter;
        }
    }
}
