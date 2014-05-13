/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.ArrayList;
import java.util.List;
import static kimble.ServerGame.SQUARES_FROM_START_TO_START;
import kimble.graphic.Camera;
import kimble.graphic.Screen;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.board.DieHolderDomeGraphic;
import kimble.graphic.board.DieHolderGraphic;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.logic.Piece;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class KimbleGraphic {

    private static final boolean AUTOMATIC_TURNS = true;

    private final KimbleLogic logic;

    private BoardGraphic board;
    private List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    private DieGraphic die;
    private DieHolderDomeGraphic dieHolderDome;

    private Camera camera;
    private Shader shader;

    private boolean running;

    private boolean executeMove = true;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;
    private float cameraPositionAngle = 0;

    public KimbleGraphic(KimbleLogic logic) {
        this.logic = logic;

        setup();
    }

    private void setup() {
        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getGame(), new BoardSpecs(SQUARES_FROM_START_TO_START));
        shader = new Shader("shader.vert", "shader.frag");

        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f((float) (Math.PI / 3.0), 0, 0), 70f, 0.3f, 1000f);
        camera.setPosition(new Vector3f(0, board.getRadius() * 1.5f, board.getRadius() * 1.2f));

        pieces = new ArrayList<>();
        for (int i = 0; i < logic.getGame().getTeams().size(); i++) {
            for (Piece p : logic.getGame().getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.TEAM_COLORS.get(i)));
            }
        }
        dieHolder = new DieHolderGraphic();
        dieHolder.rotate(0, board.getHomeSquares().get(0).getRotation().y, 0);

        die = new DieGraphic();

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

    private void update(float dt) {

        if (Screen.isCloseRequested()) {
            stop();
        }

        cameraPositionAngle += dt * 0.1;
        Vector3f cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle), board.getRadius() * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle));
        camera.setPosition(cameraPos);
        camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2, 0));

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.updateProjectionMatrixAttributes();
        }

        if (AUTOMATIC_TURNS) {

            turnTimer += dt;
            if (turnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

                if (executeMove) {
                    logic.executeMove();
                    executeMove = false;
                }

                nextTurnTimer += dt;

                if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
                    if (!logic.getGame().isGameOver()) {
                        die.setDieRoll(logic.getCurrentTurn().getDieRoll());
                        dieHolderDome.bounce();
                        turnTimer = 0;
                        nextTurnTimer = 0;
                        executeMove = true;
                    }
                }
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
