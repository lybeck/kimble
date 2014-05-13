package kimble.logic.player.testai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kimble.logic.Game;
import kimble.logic.Move;
import kimble.logic.Turn;
import kimble.logic.board.Board;
import kimble.logic.board.Square;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class TestAILasse extends KimbleAI {

    private Random random;

    public TestAILasse() {
        this.random = new Random();
    }

    @Override
    public int selectMove(Turn turn, Game game) {
        Board board = game.getBoard();
        Map<Move, Integer> map = new HashMap<>();
        for (int i = 0; i < turn.getMoves().size(); ++i) {
            Move move = turn.getMove(i);
            if (!move.isOptional()) {
                map.put(move, i);
            }
        }
        if (map.isEmpty()) {
            if (!turn.getMoves().isEmpty()) {
                return getBestInGoalMove(turn.getMoves());
            }
            return -1;
        }
        // check goal moves
        for (Move move : map.keySet()) {
            if (move.getDestination().isGoalSquare()) {
                return map.get(move);
            }
        }
        // check eat moves
        for (Move move : map.keySet()) {
            if (move.getDestination().isPiecePresent()) {
                return map.get(move);
            }
        }
        // check start moves
        for (Move move : map.keySet()) {
            if (move.getDestination().equals(board.getStartSquare(getMyTeam().getId()))) {
                return map.get(move);
            }
        }
        // check moves from start square
        for (Move move : map.keySet()) {
            if (move.getPiece().getPosition() != null && move.getPiece().getPosition().equals(board.getStartSquare(getMyTeam().getId()))) {
                return map.get(move);
            }
        }
        return getBestNormalMove(map, board);
    }

    private int getBestNormalMove(Map<Move, Integer> map, Board board) {
        Move bestMove = null;
        int bestDist = Integer.MAX_VALUE;
        for (Move move : map.keySet()) {
            int distance = getDistanceToGoal(move, board);
            if (distance < bestDist) {
                bestMove = move;
                bestDist = distance;
            }
        }
        return map.get(bestMove);
    }

    private int getDistanceToGoal(Move move, Board board) {
        Square position = move.getPiece().getPosition();
        if (position == null) {
            return Integer.MAX_VALUE - 1;
        }
        int dist = 0;
        while (!position.equals(board.getStartSquare(getMyTeam().getId()))) {
            ++dist;
            position = position.getNext();
        }
        return dist;
    }

    private int getBestInGoalMove(List<Move> moves) {
        Map<Move, Integer> map = new HashMap<>();
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            Square startPosition = move.getPiece().getPosition();
            Square endPosition = move.getDestination();
            if (startPosition.getID() < endPosition.getID()) {
                map.put(move, i);
            }
        }
        if (map.isEmpty()) {
            return -1;
        }
        int bestId = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move move : map.keySet()) {
            int id = move.getDestination().getID();
            if (id > bestId) {
                bestId = id;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            return -1;
        }
        return map.get(bestMove);
    }
}
