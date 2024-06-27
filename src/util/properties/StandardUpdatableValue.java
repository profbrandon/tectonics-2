package util.properties;

import java.util.ArrayList;
import java.util.Collection;

import util.Preconditions;

public class StandardUpdatableValue<T> implements UpdatableValue<T> {
    
    private final Collection<UpdateListener<T>> updateListeners = new ArrayList<>();

    private T value;

    public StandardUpdatableValue(final T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(final T value) {
        Preconditions.throwIfNull(value, "value");
        
        if (this.value.equals(value)) return;

        this.value = value;
        updateListeners.stream().forEach(listener -> listener.onValueUpdate(value));
    }

    @Override
    public void addUpdateListener(final UpdateListener<T> listener) {
        Preconditions.throwIfNull(listener, "listener");
        
        this.updateListeners.add(listener);
    }
}
