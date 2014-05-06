package kimble;

import java.util.Random;
import logic.Game;
import logic.GameStart;
import logic.Turn;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        runExample();
    }

    static void runExample() {
        Game game = new Game();
        Random random = new Random();
        GameStart gameStart = game.startGame();
        System.out.println("Game start:");
        System.out.println(gameStart.getRolls());
        System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
        System.out.println("--------------------------------------------------");
        System.out.println("");
        System.out.println(game);
        System.out.println("--------------------------------------------------");
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            System.out.println("Team in turn: " + game.getTeamInTurn().getId());
            Turn nextTurn = game.getNextTurn();
            System.out.println("Rolled: " + nextTurn.getDieRoll());
            if (nextTurn.getMoves().isEmpty()) {
                System.out.println("No possible moves...");
                game.executeNoMove();
            } else {
                int selection = random.nextInt(nextTurn.getMoves().size());
                game.executeMove(selection);
                System.out.println(game);
            }
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
    }
}
