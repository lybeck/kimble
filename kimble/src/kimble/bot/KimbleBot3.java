package kimble.bot;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class KimbleBot3 extends KimbleBot1 {

    /**
     * Randomness of the AI. Must have a value between 0.0 and 1.0.
     */
    private final static double RANDOMNESS = .5;

    private Random random;

    public KimbleBot3(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void preLoop() {
        super.preLoop();
    }

    @Override
    protected MoveInfo getBestMove() {
        /*
         * This bot selects randomly either a random (non-optional) move or the best move according to its super class.
         */
        if (getRandom().nextDouble() < RANDOMNESS) {
            List<MoveInfo> availableNonOptionalMoves = getAvailableNonOptionalMoves();
            if (availableNonOptionalMoves.size() < 1) {
                return getBestInGoalMove();
            } else {
                return availableNonOptionalMoves.get(random.nextInt(availableNonOptionalMoves.size()));
            }
        } else {
            return super.getBestMove();
        }
    }

    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }
}
