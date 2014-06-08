package kimble.bot;

import java.util.ArrayList;
import java.util.List;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Lasse Lybeck
 */
public abstract class AbstractKimbleBot extends KimbleClient {

    public AbstractKimbleBot(String name, String host, int port) {
        super(name, host, port);
    }

    protected abstract MoveInfo getBestMove();

    protected MoveInfo getBestInGoalMove() {
        List<MoveInfo> moves = getAvailableMoves();
        List<MoveInfo> possibleMoves = new ArrayList<>();
        for (MoveInfo move : moves) {
            if (move.isOptional() && move.getDestinationSquareId() > move.getStartSquareId()) {
                possibleMoves.add(move);
            }
        }
        if (moves.isEmpty()) {
            return null;
        }
        MoveInfo bestMove = null;
        int bestDestId = -1;
        for (MoveInfo move : possibleMoves) {
            if (move.getDestinationSquareId() > bestDestId) {
                bestDestId = move.getDestinationSquareId();
                bestMove = move;
            }
        }
        return bestMove;
    }

    protected List<MoveInfo> getAvailableNonOptionalMoves() {
        List<MoveInfo> moves = getAvailableMoves();
        List<MoveInfo> nonOptional = new ArrayList<>();
        for (MoveInfo move : moves) {
            if (!move.isOptional()) {
                nonOptional.add(move);
            }
        }
        return nonOptional;
    }

    @Override
    public void handleTurn() {
        String messageType = getReceiveMessageType();
//        System.out.println("[Team " + getMyTeamId() + "] Received: " + messageType);

        if (messageType.equals("moves")) {
            MoveInfo bestMove = getBestMove();
            if (bestMove != null) {
                sendMove(bestMove);
            } else {
                sendPing();
            }
        }
    }
}
