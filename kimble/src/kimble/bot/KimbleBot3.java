package kimble.bot;

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

    private final Random random;

    public KimbleBot3(String host, int port) {
        super("KimbleBot3", host, port);
        this.random = new Random();
    }

    @Override
    protected MoveInfo getBestMove() {
        /*
         * This bot selects randomly either a random (non-optional) move or the best move according to its super class.
         */
        if (random.nextDouble() < RANDOMNESS) {
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
}
