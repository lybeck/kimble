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
import org.lwjgl.input.Mouse;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    private BoardGraphic board;
    private Camera camera;
    private Shader shader;

    public Kimble() {

        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f(50, 220, 0), 70f, 0.3f, 1000f);

        int numberOfTeams = 4;
        int numberOfPieces = 4;
        Game game = new Game(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, numberOfTeams, numberOfPieces, 10);
        board = new BoardGraphic(game, 10, 3, 1);
        shader = new Shader("res/shaders/shader.vert", "res/shaders/shader.frag");

        List<PieceGraphic> pieces = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < game.getTeams().size(); i++) {
            for (Piece p : game.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(p, new Vector3f(r.nextFloat() * 20, 0, r.nextFloat() * 20), BoardGraphic.teamColors.get(i), 4, 10));
            }
        }

        Mouse.setGrabbed(true);
        while (!Screen.isCloseRequested()) {
            Screen.clear();

            float dt = 0.16f;

            camera.update(dt);
            board.update(dt);

            for (PieceGraphic p : pieces) {
                p.update(dt);
            }

            shader.bind();
            board.render(shader);
            for (PieceGraphic p : pieces) {
                p.render(shader);
            }
            shader.unbind();

            Screen.update(60);
        }

        shader.cleanUp();
        board.cleanUp();
        for (PieceGraphic p : pieces) {
            p.cleanUp();
        }
        Screen.cleanUp();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();

        new Kimble();

//        runExample();
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
