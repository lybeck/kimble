package kimble.logic.player.testai;

import java.util.Random;
import kimble.logic.Game;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class TestAIRandom extends KimbleAI {

    private final Random random;

    public TestAIRandom() {
        super(TestAIRandom.class.getName());
        this.random = new Random();
    }

    @Override
    public int selectMove(Turn turn, Game game) {
        int availableMoves = turn.getMoves().size();
        return random.nextInt(availableMoves);
    }
}
