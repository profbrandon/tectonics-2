package simulation;

import java.util.Collection;

import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;

public interface Simulation<T extends SimulationMode> {
    public DistinguishedTree<String, SimulationParameterGroup> getParameters();
    public void play();
    public void pause();
    public void end();
    public void step();
    public void addSimulationListeners(final Collection<SimulationListener> listeners);
    public void setMode(final T simulationMode);
}
