package util.counting;

public interface Prev<O extends Ordinal> extends Ordinal {
    
    public O next();
}
