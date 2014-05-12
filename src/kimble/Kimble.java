package kimble;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.Screen;
import kimble.logic.IPlayer;
import kimble.logic.player.testai.TestAILasse;
import kimble.logic.player.testai.TestAIRandom;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    public static void main(String[] args) {
        boolean noGui = false;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("nogui")) {
                noGui = true;
            }
        }
        if (!noGui) {
            setupLWJGL();
        }
//        new TestGame(noGui);
        
        int numberOfPlayers = 4;
        List<IPlayer> players = new ArrayList<>();
        players.add(new TestAILasse());
        for (int i = 0; i < numberOfPlayers - 1; i++) {
            players.add(new TestAIRandom());
        }
        new TestGame2(noGui, players);
    }

    private static void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }
}
