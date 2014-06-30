package kimble;

import java.util.ArrayList;
import java.util.List;
import kimble.logic.IPlayer;
import kimble.logic.player.KimblePlayer;
import kimble.logic.player.testai.FirstMoveAI;
import kimble.logic.player.testai.TestAILasse;
import kimble.logic.player.testai.TestAILasse1;
import kimble.logic.player.testai.TestAILasse2;
import kimble.logic.player.testai.TestAIRandom;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    public static void main(String[] args) {
        boolean useGui = true;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("nogui")) {
                useGui = false;
            }
        }
//        runSingleGameNoAIs();
        runSingleGameThreeAIsOnePlayer();
//        runSingleGame(useGui, Constants.DEFAULT_NUMBER_OF_TEAMS);
    }

    private static void runSingleGameNoAIs() {
        List<IPlayer> players = new ArrayList<>();

        players.add(new KimblePlayer("Test 1"));
        players.add(new KimblePlayer("Test 2"));
        players.add(new KimblePlayer("Test 3"));
        players.add(new KimblePlayer("Test 4"));

        new ServerGame(true, players).start();
    }

    private static void runSingleGameThreeAIsOnePlayer() {
        List<IPlayer> players = new ArrayList<>();

        players.add(new FirstMoveAI("First_move_AI"));
        players.add(new TestAILasse());
        players.add(new TestAILasse1());
        players.add(new KimblePlayer("Test 1"));

        new ServerGame(true, players).start();
    }

    private static void runSingleGame(boolean useGui, int numberOfPlayers) {
        List<IPlayer> players = new ArrayList<>();

        players.add(new FirstMoveAI("First_move_AI"));
        players.add(new TestAILasse());
        players.add(new TestAILasse1());
        players.add(new TestAILasse2());
        while (players.size() < numberOfPlayers) {
            players.add(new TestAIRandom());
        }

        new ServerGame(useGui, players).start();

//        int games = 100000;
//        
//        Map<Integer, Integer> wins = new HashMap<>();
//        for (int i = 0; i < 4; i++) {
//            wins.put(i, 0);
//        }
//        
//        Timer timer = new Timer();
//        timer.tic();
//        for (int i = 0; i < games; i++) {
//            ServerGame serverGame = new ServerGame(false, players);
//            serverGame.start();
//            int winner = serverGame.getLogic().getWinner();
//            wins.put(winner, wins.get(winner) + 1);
//        }
//        System.out.println("Time elapsed: " + timer.toc() + " seconds.");
//        
//        System.out.println("wins = " + wins);
    }

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
