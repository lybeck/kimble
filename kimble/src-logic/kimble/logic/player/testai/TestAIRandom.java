package kimble.logic.player.testai;

import java.util.List;
import java.util.Random;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class TestAIRandom extends KimbleAI {

    private final Random random;

    public TestAIRandom() {
        super("TestAIRandom");
        this.random = new Random();
    }

    @Override
    public int selectMove(Turn turn, List<Team> teams) {
        int availableMoves = turn.getMoves().size();
        return random.nextInt(availableMoves);
    }
}
