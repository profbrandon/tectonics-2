package simulation;

import java.util.List;

public interface SimulationMode {
    public List<SimulationMode> enumerate();
    public SimulationMode getDefaultMode();
}
