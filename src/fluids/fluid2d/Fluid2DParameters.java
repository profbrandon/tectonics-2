package fluids.fluid2d;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import simulation.SimulationParameters;
import simulation.parameters.FloatParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class Fluid2DParameters extends SimulationParameters {

    private final float DEFAULT_VISCOSITY       = 0.0f;
    private final float DEFAULT_COMPRESSIBILITY = 0.0f;

    private final FloatParameter viscosityParameter = new FloatParameter(
        "Viscosity",
        "V",
        "The viscosity of the fluid",
        DEFAULT_VISCOSITY,
        0.0f,
        1.0f);
    private final FloatParameter compressibilityParameter = new FloatParameter(
        "Compressibility",
        "Comp",
        "How much the fluid can be compressed",
        DEFAULT_COMPRESSIBILITY,
        0.0f,
        1.0f);

    private final AtomicReference<Float> viscosity       = new AtomicReference<>(DEFAULT_VISCOSITY);
    private final AtomicReference<Float> compressibility = new AtomicReference<>(DEFAULT_COMPRESSIBILITY);

    public Fluid2DParameters() {
        this.setEnableableValues(List.of(
            viscosityParameter,
            compressibilityParameter
        ));

        bindParameter(viscosityParameter, viscosity);
        bindParameter(compressibilityParameter, compressibility);
    }

    public float getViscosity() {
        return this.viscosity.get();
    }

    public float getCompressibility() {
        return this.compressibility.get();
    }

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameterTree() {
        return new DistinguishedTree<>(
            "2D Fluids",
            List.of(
                new DistinguishedTree<>(
                    new SimulationParameterGroup.Builder()
                        .addFloatParameter(viscosityParameter)
                        .addFloatParameter(compressibilityParameter)
                        .build())));
    }
}
