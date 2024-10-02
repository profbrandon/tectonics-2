package fluids.fluid2d;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import simulation.SimulationParameters;
import simulation.parameters.FloatParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class Fluid2DParameters extends SimulationParameters {

    private final float DEFAULT_VISCOSITY = 0.0f;
    private final float DEFAULT_DIFFUSION = 0.0f;

    private final FloatParameter viscosityParameter = new FloatParameter(
        "Viscosity",
        "Visc",
        "The viscosity of the fluid",
        DEFAULT_VISCOSITY,
        0.0f,
        1.0f);
    private final FloatParameter diffusionParameter = new FloatParameter(
        "Diffusion",
        "Diff",
        "How fast the density field diffuses",
        DEFAULT_DIFFUSION,
        0.0f,
        1.0f);

    private final AtomicReference<Float> viscosity       = new AtomicReference<>(DEFAULT_VISCOSITY);
    private final AtomicReference<Float> diffusion = new AtomicReference<>(DEFAULT_DIFFUSION);

    public Fluid2DParameters() {
        this.setEnableableValues(List.of(
            viscosityParameter,
            diffusionParameter
        ));

        bindParameter(viscosityParameter, viscosity);
        bindParameter(diffusionParameter, diffusion);
    }

    public float getViscosity() {
        return this.viscosity.get();
    }

    public float getDiffusion() {
        return this.diffusion.get();
    }

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameterTree() {
        return new DistinguishedTree<>(
            "2D Fluids",
            List.of(
                new DistinguishedTree<>(
                    new SimulationParameterGroup.Builder()
                        .addFloatParameter(viscosityParameter)
                        .addFloatParameter(diffusionParameter)
                        .build())));
    }
}
