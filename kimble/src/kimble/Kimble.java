package kimble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.graphic.Screen;
import kimble.logic.IPlayer;
import kimble.logic.player.testai.TestAILasse;
import kimble.logic.player.testai.TestAIRandom;
import kimble.util.Timer;

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
        
        new TestGame(noGui);

//        int numberOfPlayers = 4;
//        runSingleGame(noGui, numberOfPlayers);
//        runMultipleGames(numberOfPlayers);
    }

    private static void runSingleGame(boolean noGui, int numberOfPlayers) {
        List<IPlayer> players = new ArrayList<>();
        players.add(new TestAILasse());
        for (int i = 0; i < numberOfPlayers - 1; i++) {
            players.add(new TestAIRandom());
        }
        new TestGame2(noGui, players);

    }

    private static void runMultipleGames(int numberOfPlayers) {
        
        int numberOfGames = 1000;
        
        List<IPlayer> players = new ArrayList<>();
        players.add(new TestAILasse());
        Map<Integer, Integer> winners = new HashMap<>();
        winners.put(0, 0);
        for (int i = 1; i < numberOfPlayers; i++) {
            players.add(new TestAIRandom());
            winners.put(i, 0);
        }
        Timer timer = new Timer();
        timer.tic();
        for (int i = 0; i < numberOfGames; i++) {
            TestGame2 testGame = new TestGame2(true, players);
            int winner = testGame.getWinner();
            winners.put(winner, winners.get(winner) + 1);
        }
        double toc = timer.toc();
        System.out.println("");
        System.out.println("winners = " + winners);
        System.out.println("");
        System.out.println("Time elapsed: " + toc + " seconds.");
    }

    private static void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }
}
