package kimble.logic;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author Lasse Lybeck
 */
public class Die {

    private final int maxValue;
    private final Random random;

    public Die() {
        this.maxValue = Constants.DEFAULT_DIE_SIDES;
        random = new SecureRandom();
    }

    public int roll() {
        return random.nextInt(maxValue) + 1;
    }
}
