package logic;

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
        random = new Random();
    }

    public int roll() {
        return random.nextInt(maxValue) + 1;
    }
}
