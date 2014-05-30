package kimble.bot;

import java.io.IOException;
import java.util.List;
import kimble.connection.clientside.MoveInfo;
import kimble.connection.clientside.PieceInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class KimbleBot1 extends AbstractKimbleBot {

    public KimbleBot1(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    protected MoveInfo getBestMove() {
        /*
         * This bot chooses a move according to a list of possible actions, and selects the move that first meets a
         * criterion. If no other possible moves exists it selects the best in-goal move.
         */
        List<MoveInfo> nonOptional = getAvailableNonOptionalMoves();
        if (nonOptional.isEmpty()) {
            return getBestInGoalMove();
        }
        return getBestNormalMove(nonOptional);
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
