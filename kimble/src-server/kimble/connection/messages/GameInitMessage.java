package kimble.connection.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.connection.clientside.SquareInfo;
import kimble.logic.board.Board;
import kimble.logic.board.Square;

/**
 *
 * @author Lasse Lybeck
 */
public class GameInitMessage extends SendMessage {

    private final List<SquareInfo> squares;

    public GameInitMessage(Board board, int teams) {
        List<Square> boardSquares = board.getSquares();
        Map<Integer, Integer> startSquares = getStartSquares(board, teams);
        squares = new ArrayList<>();

        // add normal squares
        for (Square square : boardSquares) {
            int id = square.getID();
            boolean startSquare = startSquares.containsKey(id);
            if (!startSquare) {
                squares.add(new SquareInfo(id));
            } else if (startSquare) {
                int teamId = startSquares.get(id);
                squares.add(new SquareInfo(id, startSquare, false, teamId));
            }
        }

        // add goal squares
        for (int i = 0; i < teams; i++) {
            List<Square> goalSquares = board.getGoalSquares(i);
            for (Square square : goalSquares) {
                int id = square.getID();
                squares.add(new SquareInfo(id, false, true, i));
            }
        }
    }

    @Override
    protected String getType() {
        return "gameInit";
    }

    /**
     * Retrieves a map of start squares, where the squareID of each start square is mapped to the teamID.
     *
     * @param board The game board.
     * @param teams Number of teams in the game.
     * @return A map of start squares.
     */
    private Map<Integer, Integer> getStartSquares(Board board, int teams) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < teams; i++) {
            Square startSquare = board.getStartSquare(i);
            map.put(startSquare.getID(), i);
        }
        return map;
    }
}
