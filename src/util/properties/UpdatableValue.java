package util.properties;

public interface UpdatableValue<T> {
    
    public T getValue();

    public void setValue(final T value);

    public void addUpdateListener(final UpdateListener<T> listener);
}
