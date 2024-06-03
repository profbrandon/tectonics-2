package simulation.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class BasicParameterTree {
    private final List<BooleanParameter>   booleanParameters;
    private final List<ByteParameter>      byteParameters;
    private final List<ShortParameter>     shortParameters;
    private final List<IntegerParameter>   integerParameters;
    private final List<LongParameter>      longParameters;
    private final List<FloatParameter>     floatParameters;
    private final List<DoubleParameter>    doubleParameters;
    private final List<CharacterParameter> characterParameters;
    private final List<StringParameter>    stringParameters;

    private final List<BasicParameterTree> subTrees;

    private BasicParameterTree(
        final List<BooleanParameter>   booleanParameters,
        final List<ByteParameter>      byteParameters,
        final List<ShortParameter>     shortParameters,
        final List<IntegerParameter>   integerParameters,
        final List<LongParameter>      longParameters,
        final List<FloatParameter>     floatParameters,
        final List<DoubleParameter>    doubleParameters,
        final List<CharacterParameter> characterParameters,
        final List<StringParameter>    stringParameters,
        final List<BasicParameterTree> subTrees) {
        
        this.booleanParameters   = booleanParameters;
        this.byteParameters      = byteParameters;
        this.shortParameters     = shortParameters;
        this.integerParameters   = integerParameters;
        this.longParameters      = longParameters;
        this.floatParameters     = floatParameters;
        this.doubleParameters    = doubleParameters;
        this.characterParameters = characterParameters;
        this.stringParameters    = stringParameters;
        this.subTrees            = subTrees;
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

    public List<BooleanParameter> getAllBooleanParameters() {
        return this.findAll(tree -> tree.getBooleanParameters(), tree -> tree.getAllBooleanParameters());
    }

    public List<ByteParameter> getAllByteParameters() {
        return this.findAll(tree -> tree.getByteParameters(), tree -> tree.getAllByteParameters());
    }

    public List<ShortParameter> getAllShortParameters() {
        return this.findAll(tree -> tree.getShortParameters(), tree -> tree.getAllShortParameters());
    }

    public List<IntegerParameter> getAllIntegerParameters() {
        return this.findAll(tree -> tree.getIntegerParameters(), tree -> tree.getAllIntegerParameters());
    }

    public List<LongParameter> getAllLongParameters() {
        return this.findAll(tree -> tree.getLongParameters(), tree -> tree.getAllLongParameters());
    }

    public List<FloatParameter> getAllFloatParameters() {
        return this.findAll(tree -> tree.getFloatParameters(), tree -> tree.getAllFloatParameters());
    }

    public List<DoubleParameter> getAllDoubleParameters() {
        return this.findAll(tree -> tree.getDoubleParameters(), tree -> tree.getAllDoubleParameters());
    }

    public List<CharacterParameter> getAllCharacterParameters() {
        return this.findAll(tree -> tree.getCharacterParameters(), tree -> tree.getAllCharacterParameters());
    }

    public List<StringParameter> getAllStringParameters() {
        return this.findAll(tree -> tree.getStringParameters(), tree -> tree.getAllStringParameters());
    }

    public List<NamedParameter> getAllParameters() {
        return this.findAll(tree -> tree.getParameters(), tree -> tree.getAllParameters());
    }

    private <T> List<T> findAll(final Function<BasicParameterTree, List<T>> initial, final Function<BasicParameterTree, List<T>> produce) {
        final List<T> result = initial.apply(this);

        for (final BasicParameterTree subTree : subTrees) {
            result.addAll(produce.apply(subTree));
        }

        return result;
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

        private List<BasicParameterTree> subTrees = new ArrayList<>();
    
        public Builder() {
            
        }

        public BasicParameterTree build() {
            return new BasicParameterTree(
                booleanParameters,
                byteParameters, 
                shortParameters, 
                integerParameters, 
                longParameters, 
                floatParameters, 
                doubleParameters, 
                characterParameters, 
                stringParameters,
                subTrees);
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
