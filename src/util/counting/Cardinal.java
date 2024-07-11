package util.counting;

/**
 * Class for representing cardinal numbers (ones that convey the size of sets).
 */
public interface Cardinal {
    
    /**
     * @return the integer value of this cardinal
     */
    public int getInteger();

    /**
     * @return the next cardinal
     */
    public Cardinal next();

    /**
     * @return the previous cardinal
     */
    public Cardinal prev();
}
