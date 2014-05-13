package kimbleai;

import java.util.Random;
import kimble.logic.Turn;
import kimble.logic.board.Board;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class RandomAI extends KimbleAI {

    private final Random random;

    public RandomAI() {
        this.random = new Random();
    }

    @Override
    public int selectMove(Turn turn, Board board) {
        return random.nextInt(turn.getMoves().size());
    }
}
