package kimble.logic;

import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
public class Turn {

    private final int dieRoll;
    private final List<Move> moves;

    public Turn(int dieRoll, List<Move> moves) {
        this.dieRoll = dieRoll;
        this.moves = moves;
    }

    public int getDieRoll() {
        return dieRoll;
    }

    public int size() {
        return moves.size();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Move getMove(int i) {
        return moves.get(i);
    }
}
