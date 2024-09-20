package tectonics;

import java.util.Collection;
import java.util.List;

import simulation.Simulation;
import simulation.SimulationListener;
import simulation.parameters.SimulationParameterGroup;

import util.data.trees.DistinguishedTree;

public class TectonicSimulation implements Simulation<TectonicSimulationMode> {

    @Override
    public DistinguishedTree<String, SimulationParameterGroup> getParameters() {
        return new DistinguishedTree<>("No Parameters", List.of());
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

    }
    
    @Override
    public void setMode(TectonicSimulationMode simulationMode) {
        
    }
}
