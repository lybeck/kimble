package kimble.logic.player.testai;

import java.util.Random;
import kimble.logic.Turn;
import kimble.logic.board.Board;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class TestAIRandom extends KimbleAI {

    private final Random random;

    public TestAIRandom() {
        this.random = new Random();
    }

    @Override
    public int selectMove(Turn turn, Board board) {
        int availableMoves = turn.getMoves().size();
        return random.nextInt(availableMoves);
    }
}
