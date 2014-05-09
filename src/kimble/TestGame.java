package kimble;

import kimble.graphic.Camera;
import kimble.graphic.Screen;
import kimble.graphic.Shader;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.PieceGraphic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.loading.ModelManager;
import kimble.logic.Constants;
import kimble.logic.Game;
import kimble.logic.GameStart;
import kimble.logic.Piece;
import kimble.logic.Team;
import kimble.logic.Turn;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Lasse Lybeck
 */
public class TestGame {

    private Game game;

    private BoardGraphic board;
    private List<PieceGraphic> pieces;
    private DieGraphic die;

    private Camera camera;
    private Shader shader;

    private static final boolean AUTOMATIC_TURNS = true;
    private static final float TURN_TIME_STEP = 0.1f;
    private static final boolean DEBUG = false;

    public TestGame() {

        setup();

        while (!Screen.isCloseRequested()) {
            Screen.clear();

            float dt = 0.016f;

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
        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f((float) (Math.PI / 3.0), 0, 0), 70f, 0.3f, 1000f);

        int numberOfTeams = 4;
        int numberOfPieces = 4;
        int sideLength = 8;

        this.game = new Game(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, numberOfTeams, numberOfPieces, sideLength);

        ModelManager.loadModels();

        board = new BoardGraphic(game, 1, 0.3f, 0.1f, 1.15f, 1f);
        shader = new Shader("res/shaders/shader.vert", "res/shaders/shader.frag");

        camera.setPosition(new Vector3f(0, board.getRadius() * 1.5f, board.getRadius() * 1.2f));

        pieces = new ArrayList<>();
        for (int i = 0; i < game.getTeams().size(); i++) {
            for (Piece p : game.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.teamColors.get(i), 0.4f, 1f));
            }
        }
        die = new DieGraphic(game.getDie(), 1f);

        GameStart gameStart = game.startGame();
        System.out.println("Game start:");
        System.out.println(gameStart.getRolls());
        System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
        System.out.println("--------------------------------------------------");
        System.out.println("");
        System.out.println(game);
        System.out.println("--------------------------------------------------");
        System.out.println("");

//        Mouse.setGrabbed(true);
    }

    private float timer = 0;
    private float cameraPositionAngle = 0;

    private void update(float dt) {

        cameraPositionAngle += dt * 0.1;
        camera.setPosition(new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle), board.getRadius() * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle)));
        camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2, 0));

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.updateProjectionMatrixAttributes();
        }

        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                executeMove();
            }
        }
        if (AUTOMATIC_TURNS) {
            timer += dt;
            if (timer >= TURN_TIME_STEP) {
                executeMove();
                timer = 0;
            }
        }

        camera.update(dt);
        board.update(dt);

        die.update(dt);

        for (PieceGraphic p : pieces) {
            p.update(dt);
        }
    }

    Random random = new Random();

    int lastKey = -1;

    private void executeMove() {
        if (game.isGameOver()) {
            return;
        }
        Turn nextTurn = game.getNextTurn();
        if (DEBUG) {
            System.out.println("Team in turn: " + game.getTeamInTurn().getId());
            System.out.println("Rolled: " + nextTurn.getDieRoll());
        }
        if (nextTurn.getMoves().isEmpty()) {
            if (DEBUG) {
                System.out.println("No possible moves...");
            }
            game.executeNoMove();
        } else {
            int selection = random.nextInt(nextTurn.getMoves().size());
            game.executeMove(selection);
        }
        if (DEBUG) {
            System.out.println(game);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        // check if game just ended
        if (game.isGameOver()) {
            System.out.println("");
            System.out.println("Game finished!");
            System.out.println("Finishing order: ");
            for (Team team : game.getFinishedTeams()) {
                System.out.println(team.getId());
            }
            System.out.println("");
        }
    }

    private void render() {

        shader.bind();
        board.render(shader);
        die.render(shader);

        for (PieceGraphic p : pieces) {
            p.render(shader);
        }
        shader.unbind();
    }
}
