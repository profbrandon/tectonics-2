package erosion;

import java.util.List;

import simulation.SimulationMode;

public enum ErosionSimulationMode implements SimulationMode {
    HEIGHT;

    @Override
    public List<SimulationMode> enumerate() {
        return List.of(HEIGHT);
    }

    @Override
    public ErosionSimulationMode getDefaultMode() {
        return HEIGHT;
    }
    
    
}
