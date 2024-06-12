package util;

public interface UpdatableValue<T> {
    
    public T getValue();

    public void setValue(final T value);
}
