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

    private final Random random;

    public KimbleBot3(String host, int port) throws IOException {
        super(host, port);
        this.random = new Random();
    }

    @Override
    protected MoveInfo getBestMove() {
        /*
         * This bot selects randomly either a random (non-optional) move or the best move according to its super class.
         */
        if (random.nextDouble() < RANDOMNESS) {
            List<MoveInfo> availableNonOptionalMoves = getAvailableNonOptionalMoves();
            return availableNonOptionalMoves.get(random.nextInt(availableNonOptionalMoves.size()));
        } else {
            return super.getBestMove();
        }
    }
}
