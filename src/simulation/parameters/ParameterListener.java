package simulation.parameters;

public interface ParameterListener<T> {
    
    public void onValueChanged(final T value);
}
