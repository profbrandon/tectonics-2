package simulation;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import simulation.parameters.Parameter;
import simulation.parameters.SimulationParameterGroup;
import util.data.trees.DistinguishedTree;
import util.properties.EnableableValue;

public abstract class SimulationParameters {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private List<EnableableValue> enableableValues = List.of();

    public void setEnableableValues(final List<EnableableValue> values) {
        this.enableableValues = values;
    }
    
    public abstract DistinguishedTree<String, SimulationParameterGroup> getParameterTree();

    public synchronized void lock() {
        this.locked.set(true);
        this.enableableValues.forEach(v -> v.setEnabled(false));
    }

    public synchronized void unlock() {
        this.locked.set(false);
        this.enableableValues.forEach(v -> v.setEnabled(true));
    }

    protected <T> void bindParameter(final Parameter<T> parameter, final AtomicReference<T> value) {
        parameter.addUpdateListener(v -> {
            if (!this.locked.get()) value.set(v);
        });
    }
}
