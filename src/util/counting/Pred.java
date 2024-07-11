package util.counting;

/**
 * Interface to represent the predecessor of the given cardinal.
 */
public interface Pred<N extends Cardinal> extends Cardinal {
    
    /**
     * @return the cardinal that has this cardinal as its predecessor
     */
    public N next();
}
