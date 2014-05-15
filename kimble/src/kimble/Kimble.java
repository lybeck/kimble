package kimble;

import java.util.ArrayList;
import java.util.List;
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

        runSingleGame(noGui, 4);
    }

    private static void runSingleGame(boolean noGui, int numberOfPlayers) {
        List<IPlayer> players = new ArrayList<>();
//        for (int i = 0; i < numberOfPlayers; i++) {
//            players.add(new TestAIRandom());
//        }
//        new ServerGame(noGui, players).start();

        players.add(new TestAILasse());
        for (int i = 0; i < numberOfPlayers-1; i++) {
            players.add(new TestAIRandom());
        }
        new ServerGame(noGui, players).start();
    }
//
//    private static void runMultipleGames(boolean noGui, int numberOfPlayers) {
//
//        int numberOfGames = 2;
//
//        List<IPlayer> players = new ArrayList<>();
//        players.add(new TestAILasse());
//        Map<Integer, Integer> winners = new HashMap<>();
//        winners.put(0, 0);
//        for (int i = 1; i < numberOfPlayers; i++) {
//            players.add(new TestAIRandom());
//            winners.put(i, 0);
//        }
//        Timer timer = new Timer();
//        timer.tic();
//        for (int i = 0; i < numberOfGames; i++) {
//            ServerGame testGame = new ServerGame(noGui, players);
//            int winner = testGame.getLogic().getWinner();
//            winners.put(winner, winners.get(winner) + 1);
//        }
//        double toc = timer.toc();
//        System.out.println("");
//        System.out.println("winners = " + winners);
//        System.out.println("");
//        System.out.println("Time elapsed: " + toc + " seconds.");
//    }
}
