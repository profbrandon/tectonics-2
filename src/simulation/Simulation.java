package simulation;

import java.util.Collection;

import simulation.parameters.BasicParameterTree;

public interface Simulation<T extends SimulationMode> {
    public BasicParameterTree getParameters();
    public void play();
    public void pause();
    public void end();
    public void step();
    public void addSimulationListeners(Collection<SimulationListener> listeners);
}
