package kimble;

import graphic.BoardGraphic;
import graphic.Camera;
import graphic.Screen;
import logic.Game;
import org.lwjgl.util.vector.Vector3f;
import logic.Constants;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
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

        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupLWJGL();

        Camera camera = new Camera(70, new Vector3f(20, 70, -20), new Vector3f(50, 220, 0), 0.3f, 1000f);

        Game game = new Game(Constants.DEFAULT_START_VALUES, 4, 4, 8);
        BoardGraphic board = new BoardGraphic(game, 10, 5);

//        Mouse.setGrabbed(true);
        while (!Screen.isCloseRequested()) {
            Screen.clear();

            while (Mouse.next()) {
                if (Mouse.isButtonDown(1)) {
                    Mouse.setGrabbed(true);
                }
            }

            while (Keyboard.next()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                    Mouse.setGrabbed(false);
                }
            }

            GL11.glLoadIdentity();
            camera.applyTranslations();
            if (Mouse.isGrabbed()) {
                camera.processMouse(1);
                camera.processKeyboard(16, 5);
            }

            board.render();

            Screen.update(60);
        }
        // runExample();
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
