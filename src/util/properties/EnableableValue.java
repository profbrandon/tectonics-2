package util.properties;

public interface EnableableValue {
    
    public boolean isEnabled();

    public void setEnabled(final boolean enabled);

    public void addEnableListener(final EnableListener listener);
}
