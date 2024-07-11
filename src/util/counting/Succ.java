package util.counting;

/**
 * Interface to represent the successor of the given cardinal
 */
public interface Succ<N extends Cardinal> extends Cardinal {
    
    /**
     * @return the cardinal that has this cardinal as its successor
     */
    public N prev();
}
