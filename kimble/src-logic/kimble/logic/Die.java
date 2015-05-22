package kimble.logic;

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
        this.random = new Random();
    }

    public Die(int nDieSides) {
        this.maxValue = nDieSides;
        this.random = new Random();
    }

    public Die(int nDieSides, long seed) {
        this.maxValue = nDieSides;
        this.random = new Random(seed);
    }

    public int roll() {
        return random.nextInt(maxValue) + 1;
    }

    public Random getRandom() {
        return random;
    }
}
