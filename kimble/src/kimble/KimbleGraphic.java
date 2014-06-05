package kimble;

import java.awt.Font;
import java.io.IOException;
import kimble.logic.KimbleLogicInterface;
import kimble.graphic.AbstractGraphic;
import kimble.playback.PlaybackProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static kimble.ServerGame.SQUARES_FROM_START_TO_START;
import kimble.graphic.camera.Camera3D;
import kimble.graphic.ExtraInput;
import kimble.graphic.Input3D;
import kimble.graphic.Screen;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.board.DieHolderDomeGraphic;
import kimble.graphic.board.DieHolderGraphic;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.Hud2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.logic.Piece;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class KimbleGraphic extends AbstractGraphic {

    private final KimbleLogicInterface logic;

    private Hud2D hud2d;
    private BitmapFont font;
    private int lastTeamID = -1;

    private boolean endMessageShown = false;

    private BoardGraphic board;
    private List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    private DieGraphic die;
    private DieHolderDomeGraphic dieHolderDome;

    private Camera3D camera;
    private Input3D input;
    private ExtraInput extraInput;
    private Shader shader;
    private Shader textShader;

    private boolean started;

    private List<Map<Integer, Integer>> startingDieRolls;
    private Iterator<Map<Integer, Integer>> startingRollsIterator;
    private Map<Integer, Integer> startingRollMap;
    private Iterator<Integer> startingRollMapKeyIterator;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;

    // Change this variable to "0, PI/2, PI and 3/2 PI" to position the camera behind the home squares.
    private float cameraPositionAngle = (float) (Math.PI / 2);

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        this.logic = logic;

        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {

        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getBoard(), logic.getTeams(), new BoardSpecs(SQUARES_FROM_START_TO_START));
        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        camera = new Camera3D(70f, 0.1f, 1000f);
        float correctionAngle = -0.5f * board.getSegmentAngle();
        Vector3f cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle
                + correctionAngle), board.getRadius()
                * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle
                        + correctionAngle));
        camera.setPosition(cameraPos);
        camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2
                + correctionAngle, 0));

        camera.setupProjectionMatrix();

        extraInput = new ExtraInput();
        input = new Input3D(camera);

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

        hud2d = new Hud2D(logic.getTeams());
        try {
            font = FontGenerator.create("pieceLabel", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1), -0.01f);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGraphic.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load font for piece text!");
        }

    }

    @Override
    public void update(float dt) {
        if (Screen.isCloseRequested()) {
            stop();
        }

        if (logic.isGameOver()) {
            for (int i = 0; i < logic.getFinishedTeams().size(); i++) {
                Team finishedTeam = logic.getFinishedTeams().get(i);
                hud2d.setTeamInfo(finishedTeam.getId(), "Finished " + (i + 1));
            }
//            stop();
        }

        if (extraInput.rotateCamera()) {
            cameraPositionAngle += dt * 0.1;
            Vector3f cameraPos = new Vector3f(board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle), board.getRadius()
                    * 1.5f, board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle));
            camera.setPosition(cameraPos);
            camera.setRotation(new Vector3f((float) (Math.PI / 3.0), cameraPositionAngle - (float) Math.PI / 2, 0));
        }

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.setupProjectionMatrix();
            hud2d.updateViewport();
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

        hud2d.setPlaybackSpeed(PlaybackProfile.currentProfile);
        hud2d.update(dt);
    }

    private void updateExecuteMove(float dt) {

        if (executeMove) {
            logic.executeMove();
            executeMove = false;
        }

        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!logic.isGameOver()) {
                updateTeamInfo(logic.getNextTeamInTurn().getId(), logic.getDieRoll());

                die.setDieRoll(logic.getDieRoll());
                dieHolderDome.bounce();

                turnTimer = 0;
                nextTurnTimer = 0;
                executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }
            }
        }
    }

    private void updateStartingDieRoll(float dt) {
        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
            nextTurnTimer = 0;

            if (startingRollMapKeyIterator.hasNext()) {
                int teamID = startingRollMapKeyIterator.next();

                String oldMessage = hud2d.getTeamInfo(teamID);
                if (oldMessage == null || oldMessage.length() == 0) {
                    hud2d.setTeamInfo(teamID, "Rolled: " + startingRollMap.get(teamID));
                } else {
                    hud2d.appendTeamInfo(teamID, ", " + startingRollMap.get(teamID));
                }

                die.setDieRoll(startingRollMap.get(teamID));
                dieHolderDome.bounce();
            } else {
                if (startingRollsIterator.hasNext()) {
                    startingRollMap = startingRollsIterator.next();
                    startingRollMapKeyIterator = startingRollMap.keySet().iterator();
                } else {
                    started = true;
                }
            }
        }
    }

    private void updateTeamInfo(int teamID, int dieRoll) {
        for (Team team : logic.getTeams()) {
            if (team.isFinished()) {
                for (int i = 0; i < logic.getFinishedTeams().size(); i++) {
                    Team finishedTeam = logic.getFinishedTeams().get(i);
                    hud2d.setTeamInfo(finishedTeam.getId(), "Finished " + (i + 1));
                }
            } else if (team.getId() == teamID) {
                // Appends all the die rolls after each other on the hud.
                if (lastTeamID != teamID) {
                    hud2d.setTeamInfo(teamID, "Rolled: " + dieRoll);
                } else {
                    hud2d.appendTeamInfo(teamID, ", " + dieRoll);
                }
            } else {
                hud2d.setTeamInfo(team.getId(), "");
            }
        }
        lastTeamID = teamID;
    }

    @Override
    public void render() {
        shader.bind();
        board.render(shader, camera);

        dieHolder.render(shader, camera);
        die.render(shader, camera);
        dieHolderDome.render(shader, camera);

        for (PieceGraphic p : pieces) {
            p.render(shader, camera);
        }

        textShader.bind();
//        font.renderString(textShader, camera, "Hello World!", new Vector3f(0, 2, 0));
        hud2d.render(textShader);
    }

    @Override
    public void dispose() {

        shader.dispose();
        textShader.dispose();

        board.dispose();

        dieHolder.dispose();
        die.dispose();
        dieHolderDome.dispose();

        for (PieceGraphic p : pieces) {
            p.dispose();
        }

        ModelManager.dispose();
        TextureManager.dispose();

        Screen.dispose();
    }
}
