package kimble.util;

/**
 *
 * @author Lasse Lybeck
 */
public class Timer {

    private long start;

    public void tic() {
        start = System.currentTimeMillis();
    }

    public double toc() {
        return (System.currentTimeMillis() - start) / 1000.0;
    }
}
