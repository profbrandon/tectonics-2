package simulation.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimulationParameterGroup {
    private final List<BooleanParameter>   booleanParameters;
    private final List<ByteParameter>      byteParameters;
    private final List<ShortParameter>     shortParameters;
    private final List<IntegerParameter>   integerParameters;
    private final List<LongParameter>      longParameters;
    private final List<FloatParameter>     floatParameters;
    private final List<DoubleParameter>    doubleParameters;
    private final List<CharacterParameter> characterParameters;
    private final List<StringParameter>    stringParameters;

    private SimulationParameterGroup(
        final List<BooleanParameter>   booleanParameters,
        final List<ByteParameter>      byteParameters,
        final List<ShortParameter>     shortParameters,
        final List<IntegerParameter>   integerParameters,
        final List<LongParameter>      longParameters,
        final List<FloatParameter>     floatParameters,
        final List<DoubleParameter>    doubleParameters,
        final List<CharacterParameter> characterParameters,
        final List<StringParameter>    stringParameters) {
        
        this.booleanParameters   = booleanParameters;
        this.byteParameters      = byteParameters;
        this.shortParameters     = shortParameters;
        this.integerParameters   = integerParameters;
        this.longParameters      = longParameters;
        this.floatParameters     = floatParameters;
        this.doubleParameters    = doubleParameters;
        this.characterParameters = characterParameters;
        this.stringParameters    = stringParameters;
    }

    public List<BooleanParameter> getBooleanParameters() {
        return this.booleanParameters;
    }

    public List<ByteParameter> getByteParameters() {
        return this.byteParameters;
    }

    public List<ShortParameter> getShortParameters() {
        return this.shortParameters;
    }

    public List<IntegerParameter> getIntegerParameters() {
        return this.integerParameters;
    }

    public List<LongParameter> getLongParameters() {
        return this.longParameters;
    }

    public List<FloatParameter> getFloatParameters() {
        return this.floatParameters;
    }

    public List<DoubleParameter> getDoubleParameters() {
        return this.doubleParameters;
    }

    public List<CharacterParameter> getCharacterParameters() {
        return this.characterParameters;
    }

    public List<StringParameter> getStringParameters() {
        return this.stringParameters;
    }

    public List<NamedParameter> getParameters() {
        final List<NamedParameter> parameters = new ArrayList<>();
        parameters.addAll(this.booleanParameters);
        parameters.addAll(this.byteParameters);
        parameters.addAll(this.shortParameters);
        parameters.addAll(this.integerParameters);
        parameters.addAll(this.longParameters);
        parameters.addAll(this.floatParameters);
        parameters.addAll(this.doubleParameters);
        parameters.addAll(this.characterParameters);
        parameters.addAll(this.stringParameters);

        return parameters;
    }

    public static class Builder {
        private List<BooleanParameter>   booleanParameters   = new ArrayList<>();
        private List<ByteParameter>      byteParameters      = new ArrayList<>();
        private List<ShortParameter>     shortParameters     = new ArrayList<>();
        private List<IntegerParameter>   integerParameters   = new ArrayList<>();
        private List<LongParameter>      longParameters      = new ArrayList<>();
        private List<FloatParameter>     floatParameters     = new ArrayList<>();
        private List<DoubleParameter>    doubleParameters    = new ArrayList<>();
        private List<CharacterParameter> characterParameters = new ArrayList<>();
        private List<StringParameter>    stringParameters    = new ArrayList<>();
    
        public Builder() {
            
        }

        public SimulationParameterGroup build() {
            return new SimulationParameterGroup(
                booleanParameters,
                byteParameters, 
                shortParameters, 
                integerParameters, 
                longParameters, 
                floatParameters, 
                doubleParameters, 
                characterParameters, 
                stringParameters);
        }

        public Builder addBooleanParameters(final Collection<BooleanParameter> booleanParameters) {
            this.booleanParameters.addAll(booleanParameters);
            return this;
        }

        public Builder addByteParameters(final Collection<ByteParameter> byteParameters) {
            this.byteParameters.addAll(byteParameters);
            return this;
        }

        public Builder addShortParameters(final Collection<ShortParameter> shortParameters) {
            this.shortParameters.addAll(shortParameters);
            return this;
        }

        public Builder addIntegerParameters(final Collection<IntegerParameter> integerParameters) {
            this.integerParameters.addAll(integerParameters);
            return this;
        }

        public Builder addLongParameters(final Collection<LongParameter> longParameters) {
            this.longParameters.addAll(longParameters);
            return this;
        }

        public Builder addFloatParameters(final Collection<FloatParameter> floatParameters) {
            this.floatParameters.addAll(floatParameters);
            return this;
        }

        public Builder addDoubleParameters(final Collection<DoubleParameter> doubleParameters) {
            this.doubleParameters.addAll(doubleParameters);
            return this;
        }

        public Builder addCharacterParameters(final Collection<CharacterParameter> characterParameters) {
            this.characterParameters.addAll(characterParameters);
            return this;
        }

        public Builder addStringParameters(final Collection<StringParameter> stringParameters) {
            this.stringParameters.addAll(stringParameters);
            return this;
        }

        public Builder addBooleanParameter(final BooleanParameter booleanParameter) {
            this.booleanParameters.add(booleanParameter);
            return this;
        }

        public Builder addByteParameter(final ByteParameter byteParameter) {
            this.byteParameters.add(byteParameter);
            return this;
        }

        public Builder addShortParameter(final ShortParameter shortParameter) {
            this.shortParameters.add(shortParameter);
            return this;
        }

        public Builder addIntegerParameter(final IntegerParameter integerParameter) {
            this.integerParameters.add(integerParameter);
            return this;
        }

        public Builder addLongParameter(final LongParameter longParameter) {
            this.longParameters.add(longParameter);
            return this;
        }

        public Builder addFloatParameter(final FloatParameter floatParameter) {
            this.floatParameters.add(floatParameter);
            return this;
        }

        public Builder addDoubleParameter(final DoubleParameter doubleParameter) {
            this.doubleParameters.add(doubleParameter);
            return this;
        }

        public Builder addCharacterParameter(final CharacterParameter characterParameter) {
            this.characterParameters.add(characterParameter);
            return this;
        }

        public Builder addStringParameter(final StringParameter stringParameter) {
            this.stringParameters.add(stringParameter);
            return this;
        }
    }
}
