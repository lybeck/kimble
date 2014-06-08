package kimble.bot;

import java.util.List;
import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class KimbleBot2 extends AbstractKimbleBot {

    public KimbleBot2(String host, int port) {
        super("KimbleBot2", host, port);
    }

    @Override
    protected MoveInfo getBestMove() {
        /*
         * This bot always chooses one piece to move until it reaches the goal. If no possible moves for that pieces it
         * selects the best in-goal move.
         */
        List<MoveInfo> availableNonOptionalMoves = getAvailableNonOptionalMoves();
        if (!availableNonOptionalMoves.isEmpty()) {
            return availableNonOptionalMoves.get(0);
        } else {
            return getBestInGoalMove();
        }
    }
}
