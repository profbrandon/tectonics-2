package erosion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simulation.Simulation;
import simulation.SimulationListener;
import simulation.parameters.DoubleParameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public class ErosionSimulation implements Simulation<ErosionSimulationMode> {

    private ErosionSimulationMode mode = ErosionSimulationMode.HEIGHT;

    private List<SimulationListener> listeners = new ArrayList<>();

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameters() {
        return new DistinguishedTree<String, SimulationParameterGroup>(
            "Box Blur Factors",
            List.of(
                new DistinguishedTree<>(
                    new SimulationParameterGroup.Builder()
                        .addDoubleParameter(new DoubleParameter("Blur Radius", "BlRad", "The radius of effect for the given blur"))
                        .build()),
                new DistinguishedTree<>(
                    new SimulationParameterGroup.Builder()
                        .addDoubleParameter(new DoubleParameter("Blur Strength", "BlSt", "The strength of the blur to be applied"))
                        .build())));
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void end() {

    }

    @Override
    public void step() {

    }

    @Override
    public void addSimulationListeners(final Collection<SimulationListener> listeners) {
        this.listeners.addAll(listeners);
    }
    
    @Override
    public void setMode(final ErosionSimulationMode simulationMode) {
        this.mode = simulationMode;
    }
}
