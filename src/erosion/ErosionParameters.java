package erosion;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import simulation.parameters.BooleanParameter;
import simulation.parameters.FloatParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

class ErosionParameters {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    private final boolean DEFAULT_NOISE_ENABLED  = false;
    private final float   DEFAULT_NOISE_STRENGTH = 0.1f;

    private final boolean DEFAULT_BLUR_ENABLED   = false;
    private final float   DEFAULT_BLUR_STRENGTH  = 0.5f;

    private final BooleanParameter noiseEnabledParameter = new BooleanParameter(
        "Enable noise",
        "EnNs",
        "Whether random perturbations should be enabled",
        DEFAULT_NOISE_ENABLED,
        false,
        true);
    private final FloatParameter noiseStrengthParameter = new FloatParameter(
        "Noise Strength",
        "NsSt",
        "The size of the perturbations to generate",
        DEFAULT_NOISE_STRENGTH,
        0.0f,
        10.0f);
    private final BooleanParameter blurEnabledParameter = new BooleanParameter(
        "Enable Blur", 
        "EnBl",
        "Whether the blur should be enabled",
        false,
        false,
        true);
    private final FloatParameter blurStrengthParameter = new FloatParameter (
        "Blur Strength",
        "BlSt", 
        "The strength of the blur to be applied",
        DEFAULT_BLUR_STRENGTH,
        0.0f,
        1.0f);

    private boolean noiseEnabled  = DEFAULT_NOISE_ENABLED;
    private float   noiseStrength = DEFAULT_NOISE_STRENGTH;

    private boolean blurEnabled  = DEFAULT_BLUR_ENABLED;
    private float   blurStrength = DEFAULT_BLUR_STRENGTH;

    public ErosionParameters() {
        this.noiseEnabledParameter.addUpdateListener(noiseEnabled -> {
            if (!locked.get()) this.noiseEnabled = noiseEnabled;
        });
        this.noiseStrengthParameter.addUpdateListener(noiseStrength -> {
            if (!locked.get()) this.noiseStrength = noiseStrength;
        });
        this.blurEnabledParameter.addUpdateListener(blurEnabled -> {
            if (!locked.get()) this.blurEnabled = blurEnabled;
        });
        this.blurStrengthParameter.addUpdateListener(blurStrength -> {
            if (!locked.get()) this.blurStrength = blurStrength;
        });
    }

    public synchronized void lock() {
        this.locked.set(true);
        this.noiseEnabledParameter.setEnabled(false);
        this.noiseStrengthParameter.setEnabled(false);
        this.blurEnabledParameter.setEnabled(false);
        this.blurStrengthParameter.setEnabled(false);
    }

    public synchronized void unlock() {
        this.locked.set(false);
        this.noiseEnabledParameter.setEnabled(true);
        this.noiseStrengthParameter.setEnabled(true);
        this.blurEnabledParameter.setEnabled(true);
        this.blurStrengthParameter.setEnabled(true);
    }

    public boolean getNoiseEnabled() {
        return this.noiseEnabled;
    }

    public float getNoiseStrength() {
        return this.noiseStrength;
    }

    public boolean getBlurEnabled() {
        return this.blurEnabled;
    }

    public float getBlurStrength() {
        return this.blurStrength;
    }

    public DistinguishedTree<String, SimulationParameterGroup> getParameterTree() {
        return new DistinguishedTree<>(
            "Erosion",
            List.of(
                new DistinguishedTree<>(
                    "Noise",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addBooleanParameter(noiseEnabledParameter)
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(noiseStrengthParameter)
                                .build()
                        ))),
                new DistinguishedTree<>(
                    "Landslides",
                    List.of(
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addBooleanParameter(blurEnabledParameter)
                                .build()),
                        new DistinguishedTree<>(
                            new SimulationParameterGroup.Builder()
                                .addFloatParameter(blurStrengthParameter)
                                .build())))));
    }
}