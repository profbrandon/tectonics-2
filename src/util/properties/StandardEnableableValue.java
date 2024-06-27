package util.properties;

import java.util.ArrayList;
import java.util.Collection;

import util.Preconditions;

public class StandardEnableableValue implements EnableableValue, UpdatableValue<Boolean> {
    
    private final Collection<EnableListener> enableListeners = new ArrayList<>();

    private boolean isEnabled = true;

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        Preconditions.throwIfNull(enabled, "enabled");
        
        if (this.isEnabled == enabled) return;

        this.isEnabled = enabled;
        enableListeners.stream().forEach(listener -> listener.onEnableChange(enabled));
    }

    @Override
    public void addEnableListener(final EnableListener listener) {
        Preconditions.throwIfNull(listener, "listener");

        this.enableListeners.add(listener);
    }

    @Override
    public Boolean getValue() {
        return this.isEnabled();
    }

    @Override
    public void setValue(final Boolean value) {
        this.setEnabled(value);
    }

    @Override
    public void addUpdateListener(UpdateListener<Boolean> listener) {
        this.addEnableListener(enabled -> listener.onValueUpdate(enabled));
    }
}
