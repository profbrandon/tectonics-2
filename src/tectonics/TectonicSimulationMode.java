package tectonics;

import java.util.List;

import simulation.SimulationMode;

public enum TectonicSimulationMode implements SimulationMode {
    HEIGHT,
    AGE,
    PLATES;

    @Override
    public List<SimulationMode> enumerate() {
        return null;
    }

    @Override
    public SimulationMode getDefaultMode() {
        return null;
    }
    
}
