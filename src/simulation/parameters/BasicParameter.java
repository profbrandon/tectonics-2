package simulation.parameters;

import util.Preconditions;
import util.properties.EnableListener;
import util.properties.StandardEnableableValue;
import util.properties.StandardUpdatableValue;
import util.properties.UpdateListener;

public class BasicParameter<T extends Comparable<T>> extends Parameter<T> { 
    private final String NAME;
    private final String ABBREVIATION;
    private final String DESCRIPTION;
    
    private final T DEFAULT_VALUE;
    private final T MIN_VALUE;
    private final T MAX_VALUE;

    private final StandardEnableableValue enabledProperty = new StandardEnableableValue();
    private final StandardUpdatableValue<T> valueProperty;

    public BasicParameter(
        final String name,
        final String abbreviation,
        final String description,
        final T defaultValue,
        final T minValue,
        final T maxValue) {

        Preconditions.throwIfNull(name, "name");
        Preconditions.throwIfNull(abbreviation, "abbreviation");
        Preconditions.throwIfNull(description, "description");
        Preconditions.throwIfNull(defaultValue, "defaultValue");
        Preconditions.throwIfNull(minValue, "minValue");
        Preconditions.throwIfNull(maxValue, "maxValue");

        this.NAME          = name;
        this.ABBREVIATION  = abbreviation;
        this.DESCRIPTION   = description;
        this.DEFAULT_VALUE = defaultValue;
        this.MIN_VALUE     = minValue;
        this.MAX_VALUE     = maxValue;

        this.valueProperty = new StandardUpdatableValue<>(defaultValue);
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
        Preconditions.throwIfNull(parameter, "parameter");

        if (this.isEnabled() && parameter.compareTo(this.MIN_VALUE) >= 0 && parameter.compareTo(this.MAX_VALUE) <= 0) {
            this.valueProperty.setValue(parameter);
        }
    }

    @Override
    public T getValue() {
        return this.valueProperty.getValue();
    }

    @Override
    public void addUpdateListener(final UpdateListener<T> listener) {
        this.valueProperty.addUpdateListener(listener);
    }

    @Override
    public boolean isEnabled() {
        return this.enabledProperty.isEnabled();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabledProperty.setEnabled(enabled);
    }

    @Override
    public void addEnableListener(final EnableListener listener) {
        this.enabledProperty.addEnableListener(listener);
    }
}
