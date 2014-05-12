package kimble;

import kimble.graphic.Camera;
import kimble.graphic.Screen;
import kimble.graphic.shader.Shader;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.PieceGraphic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.board.DieHolderDomeGraphic;
import kimble.graphic.board.DieHolderGraphic;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
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
    private Turn nextTurn;

    private BoardGraphic board;
    private List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    private DieGraphic die;
    private DieHolderDomeGraphic dieHolderDome;

    private Camera camera;
    private Shader shader;

    private static final boolean AUTOMATIC_TURNS = true;
    private static final float TURN_TIME_STEP = 2f;
    private static final boolean DEBUG = true;

    private boolean running;

    private float timer = 0;
    private float cameraPositionAngle = 0;
    private boolean rotateCamera = false;
    private Vector3f cameraPos;

    public final static int NUMBER_OF_TEAMS = 4;
    public final static int NUMBER_OF_PIECES = 4;
    public final static int SQUARES_FROM_START_TO_START = 8;

    public TestGame(boolean noGui) {

        if (noGui) {
            setupLogic();

            while (!game.isGameOver()) {
                executeMove();
            }
        } else {
            setupLogic();
            setupGraphic();

            start();
        }

    }

    private void setupLogic() {

        this.game = new Game(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, NUMBER_OF_TEAMS, NUMBER_OF_PIECES, SQUARES_FROM_START_TO_START);

        GameStart gameStart = game.startGame();
        System.out.println("Game start:");
        System.out.println(gameStart.getRolls());
        System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
        System.out.println("--------------------------------------------------");
        System.out.println("");
        System.out.println(game);
        System.out.println("--------------------------------------------------");
        System.out.println("");
        
        this.nextTurn = game.getNextTurn();

//        Mouse.setGrabbed(true);
    }

    private void setupGraphic() {
        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(game, new BoardSpecs(SQUARES_FROM_START_TO_START));
        shader = new Shader("shader.vert", "shader.frag");

        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f((float) (Math.PI / 3.0), 0, 0), 70f, 0.3f, 1000f);
        camera.setPosition(new Vector3f(0, board.getRadius() * 1.5f, board.getRadius() * 1.2f));
        cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(0), board.getRadius() * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(0));

        pieces = new ArrayList<>();
        for (int i = 0; i < game.getTeams().size(); i++) {
            for (Piece p : game.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.TEAM_COLORS.get(i)));
            }
        }
        dieHolder = new DieHolderGraphic();
        dieHolder.rotate(0, board.getHomeSquares().get(0).getRotation().y, 0);

        die = new DieGraphic();
        this.die.setDieRoll(nextTurn.getDieRoll());

        dieHolderDome = new DieHolderDomeGraphic();
    }

    public final void start() {
        if (this.running) {
            return;
        }
        this.running = true;
        loop();
    }

    public void stop() {
        this.running = false;
    }

    public void loop() {

        while (running) {
            Screen.clear();

            float dt = 0.016f;

            update(dt);
            render();

            Screen.update(60);
        }

        cleanUp();
    }

    private boolean cameraInPosition;

    private void update(float dt) {

        if (Screen.isCloseRequested()) {
            stop();
        }

        if (rotateCamera) {

            if (cameraPos.x - camera.getPosition().x < 0.05f && cameraPos.z - camera.getPosition().z < 0.05f) {
                cameraInPosition = true;
            }

            cameraPositionAngle += dt * 0.1;
            cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle), board.getRadius() * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle));

//            float pitch = (float) (Math.PI / 3.0);
//            float yaw = cameraPositionAngle - (float) Math.PI / 2;
//            float roll = 0;
//            camera.setRotation(new Vector3f(pitch, yaw, roll));
            if (cameraInPosition) {
                camera.setPosition(cameraPos);
                camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2, 0));
            } else {
                camera.move((cameraPos.x - camera.getPosition().x) * dt, 0, (cameraPos.z - camera.getPosition().z) * dt);
            }
        }

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.updateProjectionMatrixAttributes();
        }

        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                die.setDieRoll(nextTurn.getDieRoll());
                executeMove();
            } else if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                rotateCamera = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                rotateCamera = false;
                cameraInPosition = false;
            }
        }
        if (AUTOMATIC_TURNS) {
            timer += dt;
            if (timer >= TURN_TIME_STEP) {
                die.setDieRoll(nextTurn.getDieRoll());
                executeMove();
                timer = 0;
            }
        }

        camera.update(dt);
        board.update(dt);

        dieHolder.update(dt);
        die.update(dt);
        dieHolderDome.update(dt);

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
        } else {
            this.nextTurn = game.getNextTurn();
            this.die.setDieRoll(nextTurn.getDieRoll());
        }
    }

    private void render() {

        shader.bind();
        board.render(shader);

        dieHolder.render(shader);
        die.render(shader);
        dieHolderDome.render(shader);

        for (PieceGraphic p : pieces) {
            p.render(shader);
        }
        shader.unbind();
    }

    private void cleanUp() {

        shader.cleanUp();
        board.cleanUp();

        dieHolder.cleanUp();
        die.cleanUp();
        dieHolderDome.cleanUp();

        for (PieceGraphic p : pieces) {
            p.cleanUp();
        }

        ModelManager.cleanUp();
        TextureManager.cleanUp();

        Screen.cleanUp();
    }
}
