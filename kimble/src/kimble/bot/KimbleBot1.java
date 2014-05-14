package kimble.bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.clientside.MoveInfo;
import kimble.connection.clientside.PieceInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class KimbleBot1 extends KimbleClient {

    public KimbleBot1(String hostAddress, int port) throws IOException {
        super(hostAddress, port);
    }

    @Override
    public void preLoop() {
    }

    @Override
    public void duringLoop() {
        String messageType = getReceiveMessageType();
        System.out.println("Recieved: " + messageType);

        if (messageType.equals("moves")) {
            MoveInfo bestMove = getBestMove();
            if (bestMove != null) {
                sendMove(bestMove);
            } else {
                sendPing();
            }
        }
    }

    @Override
    public void postLoop() {
    }

    private MoveInfo getBestMove() {
        List<MoveInfo> moves = getAvailableMoves();
        List<MoveInfo> nonOptional = new ArrayList<>();
        for (MoveInfo move : moves) {
            if (!move.isOptional()) {
                nonOptional.add(move);
            }
        }
        if (nonOptional.isEmpty()) {
            return getBestInGoalMove(moves);
        }
        return getBestNormalMove(nonOptional);
    }

    private MoveInfo getBestInGoalMove(List<MoveInfo> moves) {

//        System.out.println("Chose best IN GOAL move.");
        List<MoveInfo> possibleMoves = new ArrayList<>();
        for (MoveInfo move : moves) {
            if (move.getDestinationSquareId() > move.getStartSquareId()) {
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

    private MoveInfo getBestNormalMove(List<MoveInfo> moves) {

        // check goal moves
        for (MoveInfo move : moves) {
            if (getGoalSquares(getMyTeamId()).contains(move.getDestinationSquareId())) {
//                System.out.println("Found GOAL move!");
                return move;
            }
        }

        // check eat moves
        for (MoveInfo move : moves) {
            for (PieceInfo pieceInfo : getPieceInfo()) {
                if (pieceInfo.getTeamId() != getMyTeamId() && pieceInfo.getSquareId() != null
                        && pieceInfo.getSquareId() == move.getDestinationSquareId()) {
//                    System.out.println("Found EAT move!");
                    return move;
                }
            }
        }

        // check start moves
        for (MoveInfo move : moves) {
            if (move.getDestinationSquareId() == getStartSquare(getMyTeamId()).getSquareId()) {
//                System.out.println("Found START move!");
                return move;
            }
        }

        // check moves from start square
        for (MoveInfo move : moves) {
            if (move.getStartSquareId() == getStartSquare(getMyTeamId()).getSquareId()) {
//                System.out.println("Found FROM START move!");
                return move;
            }
        }

//        System.out.println("Had to choose the most advanced move.");
        // find most advanced move
        int bestDestId = -1;
        MoveInfo bestMove = null;
        for (MoveInfo move : moves) {
            if (move.getDestinationSquareId() > bestDestId) {
                bestDestId = move.getDestinationSquareId();
                bestMove = move;
            }
        }
        return bestMove;
    }
}
