package kimble.util;

/**
 *
 * @author Christoffer
 */
public class MathHelper {

    private static final double epsilon = 0.001;

    /**
     * Returns a value between 'a' and 'b' with increment step 'dt'
     *
     * @param a
     * @param b
     * @param dt
     * @return
     */
    public static float lerp(float a, float b, float dt) {

        if (Math.abs(b - a) < epsilon) {
            return b;
        }
        return a + dt * (b - a);
    }

}
