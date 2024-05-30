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

    private <T> List<T> findAll(final Function<BasicParameterTree, List<T>> initial, final Function<BasicParameterTree, List<T>> produce) {
        final List<T> result = initial.apply(this);

        for (final BasicParameterTree subTree : subTrees) {
            result.addAll(produce.apply(subTree));
        }

        return result;
    }

    public class Builder {
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

        public void addBooleanParameters(final Collection<BooleanParameter> booleanParameters) {
            this.booleanParameters.addAll(booleanParameters);
        }

        public void addByteParameters(final Collection<ByteParameter> byteParameters) {
            this.byteParameters.addAll(byteParameters);
        }

        public void addShortParameters(final Collection<ShortParameter> shortParameters) {
            this.shortParameters.addAll(shortParameters);
        }

        public void addIntegerParameters(final Collection<IntegerParameter> integerParameters) {
            this.integerParameters.addAll(integerParameters);
        }

        public void addLongParameters(final Collection<LongParameter> longParameters) {
            this.longParameters.addAll(longParameters);
        }

        public void addFloatParameters(final Collection<FloatParameter> floatParameters) {
            this.floatParameters.addAll(floatParameters);
        }

        public void addDoubleParameters(final Collection<DoubleParameter> doubleParameters) {
            this.doubleParameters.addAll(doubleParameters);
        }

        public void addCharacterParameters(final Collection<CharacterParameter> characterParameters) {
            this.characterParameters.addAll(characterParameters);
        }

        public void addStringParameters(final Collection<StringParameter> stringParameters) {
            this.stringParameters.addAll(stringParameters);
        }

        public void addBooleanParameter(final BooleanParameter booleanParameter) {
            this.booleanParameters.add(booleanParameter);
        }

        public void addByteParameter(final ByteParameter byteParameter) {
            this.byteParameters.add(byteParameter);
        }

        public void addShortParameter(final ShortParameter shortParameter) {
            this.shortParameters.add(shortParameter);
        }

        public void addIntegerParameter(final IntegerParameter integerParameter) {
            this.integerParameters.add(integerParameter);
        }

        public void addLongParameter(final LongParameter longParameter) {
            this.longParameters.add(longParameter);
        }

        public void addFloatParameter(final FloatParameter floatParameter) {
            this.floatParameters.add(floatParameter);
        }

        public void addDoubleParameter(final DoubleParameter doubleParameter) {
            this.doubleParameters.add(doubleParameter);
        }

        public void addCharacterParameter(final CharacterParameter characterParameter) {
            this.characterParameters.add(characterParameter);
        }

        public void addStringParameter(final StringParameter stringParameter) {
            this.stringParameters.add(stringParameter);
        }
    }
}
