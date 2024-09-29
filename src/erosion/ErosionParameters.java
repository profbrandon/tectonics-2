package erosion;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import simulation.parameters.FloatParameter;
import simulation.parameters.Parameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;
import util.properties.EnableableValue;

class ErosionParameters {
    private final AtomicBoolean locked = new AtomicBoolean(false);

    private final float DEFAULT_INITIAL_HEIGHT     = 15f;
    private final float DEFAULT_INITIAL_NOISE      = 0.5f;
    private final float DEFAULT_NOISE_STRENGTH     = 0.0f;
    private final float DEFAULT_BLUR_STRENGTH      = 0.0f;
    private final float DEFAULT_ERODIBILITY        = 0.1f;
    private final float DEFAULT_SEDIMENT_TRANSPORT = 0.9f;
    private final float DEFAULT_CARVING_FACTOR     = 0.5f;

    private final FloatParameter initialHeightParameter = new FloatParameter(
        "Initial Height",
        "InitH",
        "The initial height of the terrain",
        DEFAULT_INITIAL_HEIGHT,
        0f,
        100f);
    private final FloatParameter initialNoiseParameter = new FloatParameter(
        "Initial Noise",
        "InitNs",
        "Initial noise value",
        DEFAULT_INITIAL_NOISE,
        0.0f,
        10.0f);
    private final FloatParameter noiseStrengthParameter = new FloatParameter(
        "Noise Strength",
        "NsSt",
        "The size of the perturbations to generate",
        DEFAULT_NOISE_STRENGTH,
        0.0f,
        10.0f);
    private final FloatParameter blurStrengthParameter = new FloatParameter (
        "Blur Strength",
        "BlSt", 
        "The strength of the blur to be applied",
        DEFAULT_BLUR_STRENGTH,
        0.0f,
        1.0f);
    private final FloatParameter erodibilityParameter = new FloatParameter(
        "Erodibility",
        "Erd",
        "How easily material is eroded",
        DEFAULT_ERODIBILITY,
        0.0f,
        1.0f);
    private final FloatParameter sedimentTransportParameter = new FloatParameter(
        "Sediment Transport",
        "SedTrns",
        "How well sediment is transported from one location to the next",
        DEFAULT_SEDIMENT_TRANSPORT,
        0.0f,
        1.0f);
    private final FloatParameter carvingFactorParameter = new FloatParameter(
        "Carving Factor",
        "CarFct", 
        "How well rivers carve out canyons",
        DEFAULT_CARVING_FACTOR,
        0.0f,
        1.0f);
    
    private final List<EnableableValue> enableableValues = List.of(
        initialHeightParameter,
        initialNoiseParameter,
        noiseStrengthParameter,
        blurStrengthParameter,
        erodibilityParameter,
        sedimentTransportParameter,
        carvingFactorParameter
    );

    private AtomicReference<Float> initialHeight     = new AtomicReference<>(DEFAULT_INITIAL_HEIGHT);
    private AtomicReference<Float> initialNoise      = new AtomicReference<>(DEFAULT_INITIAL_NOISE);
    private AtomicReference<Float> noiseStrength     = new AtomicReference<>(DEFAULT_NOISE_STRENGTH);
    private AtomicReference<Float> blurStrength      = new AtomicReference<>(DEFAULT_BLUR_STRENGTH);
    private AtomicReference<Float> erodibility       = new AtomicReference<>(DEFAULT_ERODIBILITY);
    private AtomicReference<Float> sedimentTransport = new AtomicReference<>(DEFAULT_SEDIMENT_TRANSPORT);
    private AtomicReference<Float> carvingFactor     = new AtomicReference<>(DEFAULT_CARVING_FACTOR);

    public ErosionParameters() {
        bindParameter(initialHeightParameter, initialHeight);
        bindParameter(initialNoiseParameter, initialNoise);
        bindParameter(noiseStrengthParameter, noiseStrength);
        bindParameter(blurStrengthParameter, blurStrength);
        bindParameter(erodibilityParameter, erodibility);
        bindParameter(sedimentTransportParameter, sedimentTransport);
        bindParameter(carvingFactorParameter, carvingFactor);
    }

    public synchronized void lock() {
        this.locked.set(true);
        this.enableableValues.forEach(v -> v.setEnabled(false));
    }

    public synchronized void unlock() {
        this.locked.set(false);
        this.enableableValues.forEach(v -> v.setEnabled(true));
    }

    public float getInitialHeight() {
        return this.initialHeight.get();
    }

    public float getInitialNoise() {
        return this.initialNoise.get();
    }

    public float getNoiseStrength() {
        return this.noiseStrength.get();
    }

    public float getBlurStrength() {
        return this.blurStrength.get();
    }

    public float getErodibility() {
        return this.erodibility.get();
    }

    public float getSedimentTransport() {
        return this.sedimentTransport.get();
    }

    public float getCarvingFactor() {
        return this.carvingFactor.get();
    }

    public DistinguishedTree<String, SimulationParameterGroup> getParameterTree() {
        return new DistinguishedTree<>(
            "Erosion",
            List.of(
                new DistinguishedTree<>(
                    new SimulationParameterGroup.Builder()
                        .addFloatParameter(initialHeightParameter)
                        .build()),
                new DistinguishedTree<>(
                    "Noise",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(initialNoiseParameter)
                                .addFloatParameter(noiseStrengthParameter)
                                .build()
                        ))),
                new DistinguishedTree<>(
                    "Landslides",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(blurStrengthParameter)
                                .build()))),
                new DistinguishedTree<>(
                    "Fluvial",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(erodibilityParameter)
                                .addFloatParameter(sedimentTransportParameter)
                                .addFloatParameter(carvingFactorParameter)
                                .build())))));
    }

    private <T> void bindParameter(final Parameter<T> parameter, final AtomicReference<T> value) {
        parameter.addUpdateListener(v -> {
            if (!this.locked.get()) value.set(v);
        });
    }
}