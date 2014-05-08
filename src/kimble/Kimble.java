package kimble;

import graphic.board.BoardGraphic;
import graphic.Screen;
import graphic.Camera;
import graphic.Shader;
import graphic.board.PieceGraphic;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import logic.Constants;
import java.util.Random;
import logic.Game;
import logic.GameStart;
import logic.Piece;
import logic.Turn;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    private Game game;

    private BoardGraphic board;
    private List<PieceGraphic> pieces;

    private Camera camera;
    private Shader shader;

    public Kimble() {

        setup();

        while (!Screen.isCloseRequested()) {
            Screen.clear();

            float dt = 0.16f;

            update(dt);
            render();

            Screen.update(60);
        }

        shader.cleanUp();
        board.cleanUp();
        for (PieceGraphic p : pieces) {
            p.cleanUp();
        }
        Screen.cleanUp();
    }

    private void setup() {
        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f(50, 220, 0), 70f, 0.3f, 1000f);

        int numberOfTeams = 7;
        int numberOfPieces = 8;
        int sideLength = 8;

        this.game = new Game(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, numberOfTeams, numberOfPieces, sideLength);

        board = new BoardGraphic(game, 10, 3, 1);
        shader = new Shader("res/shaders/shader.vert", "res/shaders/shader.frag");

        pieces = new ArrayList<>();
        for (int i = 0; i < game.getTeams().size(); i++) {
            for (Piece p : game.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.teamColors.get(i), 4, 10));
            }
        }

        GameStart gameStart = game.startGame();
        System.out.println("Game start:");
        System.out.println(gameStart.getRolls());
        System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
        System.out.println("--------------------------------------------------");
        System.out.println("");
        System.out.println(game);
        System.out.println("--------------------------------------------------");
        System.out.println("");

        Mouse.setGrabbed(true);
    }

    private void update(float dt) {
        executeMove();
        camera.update(dt);
        board.update(dt);

        for (PieceGraphic p : pieces) {
            p.update(dt);
        }
    }

    Random random = new Random();

    int lastKey = -1;

    private void executeMove() {
        while (Keyboard.next()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
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

    private void render() {

        shader.bind();
        board.render(shader);
        for (PieceGraphic p : pieces) {
            p.render(shader);
        }
        shader.unbind();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();

        new Kimble();
    }

//    static void runExample() {
//        Game game = new Game();
//        Random random = new Random();
//        GameStart gameStart = game.startGame();
//        System.out.println("Game start:");
//        System.out.println(gameStart.getRolls());
//        System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
//        System.out.println("--------------------------------------------------");
//        System.out.println("");
//        System.out.println(game);
//        System.out.println("--------------------------------------------------");
//        System.out.println("");
//        for (int i = 0; i < 100; i++) {
//            System.out.println("Team in turn: " + game.getTeamInTurn().getId());
//            Turn nextTurn = game.getNextTurn();
//            System.out.println("Rolled: " + nextTurn.getDieRoll());
//            if (nextTurn.getMoves().isEmpty()) {
//                System.out.println("No possible moves...");
//                game.executeNoMove();
//            } else {
//                int selection = random.nextInt(nextTurn.getMoves().size());
//                game.executeMove(selection);
//                System.out.println(game);
//            }
//            System.out.println("--------------------------------------------------");
//            System.out.println("");
//        }
//    }
}
