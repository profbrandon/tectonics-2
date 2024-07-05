package util.counting;

public interface Succ<N extends Cardinal> extends Cardinal {
    
    public N prev();
}
