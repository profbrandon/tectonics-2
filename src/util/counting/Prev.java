package util.counting;

public interface Prev<N extends Cardinal> extends Cardinal {
    
    public N next();
}
