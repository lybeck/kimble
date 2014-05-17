/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import kimble.playback.PlaybackProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static kimble.ServerGame.SQUARES_FROM_START_TO_START;
import kimble.graphic.Camera;
import kimble.graphic.ExtraInput;
import kimble.graphic.Input;
import kimble.graphic.Screen;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.board.DieHolderDomeGraphic;
import kimble.graphic.board.DieHolderGraphic;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.HUD;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.logic.Move;
import kimble.logic.Piece;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class KimbleGraphic {

    private final KimbleLogicInterface logic;

    private HUD hud;
    private BoardGraphic board;
    private List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    private DieGraphic die;
    private DieHolderDomeGraphic dieHolderDome;

    private Camera camera;
    private Input input;
    private ExtraInput extraInput;
    private Shader shader;

    private boolean running;
    private boolean started;

    private List<Map<Integer, Integer>> startingDieRolls;
    private Iterator<Map<Integer, Integer>> startingRollsIterator;
    private Map<Integer, Integer> startingRollMap;
    private Iterator<Integer> startingRollMapKeyIterator;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;
    private float cameraPositionAngle = 0;
    private boolean endMessageShown = false;

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        this.logic = logic;

        PlaybackProfile.setCurrentProfile(profile);

        setup();
    }

    private void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }

    private void setup() {
        setupLWJGL();
        setupHUD();

        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getBoard(), logic.getTeams(), new BoardSpecs(SQUARES_FROM_START_TO_START));
        shader = new Shader("shader.vert", "shader.frag");

        camera = new Camera(new Vector3f(20, 70, -20), new Vector3f((float) (Math.PI / 3.0), 0, 0), 70f, 0.3f, 1000f);

        extraInput = new ExtraInput();
        input = new Input(camera);

        pieces = new ArrayList<>();
        for (int i = 0; i < logic.getTeams().size(); i++) {
            for (Piece p : logic.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.TEAM_COLORS.get(i)));
            }
        }
        dieHolder = new DieHolderGraphic();
        dieHolder.rotate(0, board.getHomeSquares().get(0).getRotation().y, 0);

        die = new DieGraphic();

        dieHolderDome = new DieHolderDomeGraphic();

        startingDieRolls = logic.getStartingDieRolls();
        startingRollsIterator = startingDieRolls.iterator();
        startingRollMap = startingRollsIterator.next();
        startingRollMapKeyIterator = startingRollMap.keySet().iterator();

        hud.appendLine("===============================");
        hud.appendLine("Rolling for starting order");
        hud.appendLine("===============================\n");

        started = false;
    }

    private void setupHUD() {
        hud = new HUD();
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

//        if(logic.getGame().isGameOver()){
//            stop();
//        }
        if (extraInput.rotateCamera()) {
            cameraPositionAngle += dt * 0.1;
            Vector3f cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle), board.getRadius()
                    * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle));
            camera.setPosition(cameraPos);
            camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2, 0));
        }

        if (Screen.wasResized()) {
            Screen.updateViewport();
            hud.setViewport(0, 0, Screen.getWidth(), Screen.getHeight());
            camera.updateProjectionMatrixAttributes();
        }

        turnTimer += dt;
        if (turnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
            if (started) {
                updateExecuteMove(dt);
            } else {
                updateStartingDieRoll(dt);
            }
        }

        extraInput.update(dt);
        input.update(dt);
        camera.update(dt);

        board.update(dt);
        dieHolder.update(dt);
        die.update(dt);
        dieHolderDome.update(dt);

        for (PieceGraphic p : pieces) {
            p.update(dt);
        }

        hud.update(dt);
    }

    private Team nextTeam;
    private Move selectedMove;

    private void updateExecuteMove(float dt) {
        StringBuilder sb;

        if (executeMove) {
            logic.executeMove();
            executeMove = false;

            selectedMove = logic.getSelectedMove();

            // append the move info based on the move selection.
            sb = new StringBuilder();
            if (selectedMove == null) {
                sb.append(": ")
                        .append(logic.getMoveMessage());
            } else {
                sb.append(": Piece [")
                        .append(selectedMove.getPiece().getId())
                        .append("] from [")
                        .append(selectedMove.getOldPositionID())
                        .append("] to [")
                        .append(selectedMove.getDestination().getID())
                        .append("]");
            }
            sb.append(" (")
                    .append(nextTeam.getName())
                    .append(")\n");
            hud.appendLine(sb);
        }

        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!logic.isGameOver()) {
                nextTeam = logic.getNextTeamInTurn();

                // Append a new die roll to the info-stream in gui
                sb = new StringBuilder();
                sb.append("[ID = ")
                        .append(nextTeam.getId())
                        .append("] rolled ")
                        .append(logic.getDieRoll());
                hud.appendLine(sb);

                die.setDieRoll(logic.getDieRoll());
                dieHolderDome.bounce();
                turnTimer = 0;
                nextTurnTimer = 0;
                executeMove = true;
            } else {
                if (!endMessageShown) {
                    sb = new StringBuilder();
                    sb.append("\n")
                            .append("===============================")
                            .append("\n")
                            .append("Finnishing order: ");

                    int size = logic.getFinnishedTeams().size();
                    for (int i = 0; i < size; i++) {
                        sb.append("[").append(logic.getFinnishedTeams().get(i).getId()).append("]");
                        if (i < size - 1) {
                            sb.append(", ");
                        }
                    }

                    sb.append("\n")
                            .append("Winner: ")
                            .append("[").append(logic.getWinner()).append("]")
                            .append("\n")
                            .append("===============================");
                    hud.appendLine(sb);
                    endMessageShown = true;
                }
            }
        }
    }

    private void updateStartingDieRoll(float dt) {
        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
            nextTurnTimer = 0;

            if (startingRollMapKeyIterator.hasNext()) {
                int teamID = startingRollMapKeyIterator.next();
                int dieRoll = startingRollMap.get(teamID);

                hud.appendLine(new StringBuilder().append("[ID = ").append(teamID).append("] rolled ").append(dieRoll).append(" (").append(logic.getTeam(teamID).getName()).append(")\n"));

                die.setDieRoll(dieRoll);
                dieHolderDome.bounce();
            } else {
                if (startingRollsIterator.hasNext()) {
                    startingRollMap = startingRollsIterator.next();
                    startingRollMapKeyIterator = startingRollMap.keySet().iterator();
                    hud.appendLine("");
                } else {
                    Team startingTeam = logic.getStartingTeam();
                    hud.appendLine(new StringBuilder().append("\n[ID = ").append(startingTeam.getId()).append("] starts the game (").append(startingTeam.getName()).append("\n"));
                    hud.appendLine("===============================");
                    hud.appendLine("");
                    started = true;
                }
            }
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

        hud.render();
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
