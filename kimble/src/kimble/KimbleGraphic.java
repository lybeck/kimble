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
import kimble.playback.PlaybackLogic;
import kimble.util.MathHelper;
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

    private boolean showTags = false;
    private boolean moveAuto = true;

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

    private float cameraPositionAngle;

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        this.logic = logic;
        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {

        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getBoard(), logic.getTeams(), new BoardSpecs());
        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        camera = new Camera3D(70f, 0.1f, 1000f);
        rotateCameraToTeam(0);

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

        hud2d = new Hud2D(this, logic.getTeams());
        try {
            font = FontGenerator.create("pieceLabel", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1), -0.02f);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGraphic.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load font for piece text!");
        }

    }

    private void updateCameraPosition() {

        Vector3f cameraPos = new Vector3f(
                board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle),
                board.getRadius() * 1.5f,
                board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle)
        );
        Vector3f rotation = new Vector3f(
                (float) (Math.PI / 3),
                cameraPositionAngle - (float) (Math.PI / 2),
                0
        );

        camera.setPosition(cameraPos);
        camera.setRotation(rotation);
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

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.setupProjectionMatrix();
            hud2d.updateViewport();
        }

        extraInput.update(dt);
        input.update(dt);

        if (extraInput.isRotateCamera()) {
            cameraPositionAngle += dt * 0.1;
            updateCameraPosition();
        }

        if (extraInput.isUpdateCameraPosition()) {
            updateCameraAngle(dt);
            updateCameraPosition();
        }
        camera.update(dt);

        if (moveAuto) {
            turnTimer += dt;
            if (turnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
                if (started) {
                    updateExecuteMove(dt);
                } else {
                    updateStartingDieRoll(dt);
                }
            }
        } else {
            if (started) {
                if (logic instanceof PlaybackLogic) {
                    updateExecuteMovePlayback();
                } else {
                    updateExecuteMoveManual();
                }
            } else {
                // TODO: Make this method a manual one as well! (Needed if people wants to "believe that they rolled the die)
                updateStartingDieRoll(dt);
            }
        }

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

        executeMoveLogic();
        if (logic instanceof PlaybackLogic) {
            ((PlaybackLogic) logic).getNextMove();
        }

        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!logic.isGameOver()) {
                updateTeamInfo(logic.getNextTeamInTurn().getId(), logic.getDieRoll());

                updateDieRoll();

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

    private void updateExecuteMoveManual() {

        if (extraInput.isExecuteNextMove()) {
            executeMoveLogic();
            extraInput.setExecuteNextMove(false);

            updateTeamInfo(logic.getNextTeamInTurn().getId(), logic.getDieRoll());
            updateDieRoll();
        }
    }

    private void executeMoveLogic() {
        if (executeMove) {
            logic.executeMove();
            executeMove = false;
        }
    }

    private void updateExecuteMovePlayback() {

        // TODO: now it works... kind of... still an odd case when moving the same piece back and forth.
        // TODO: this will cause the "Rolled: dieRoll" label to append the same roll twice.
        if (extraInput.isExecuteNextMove()) {

            logic.executeMove();
            ((PlaybackLogic) logic).getNextMove();
            extraInput.setExecuteNextMove(false);

            updateTeamInfo(logic.getNextTeamInTurn().getId(), logic.getDieRoll());
            updateDieRoll();

        } else if (extraInput.isExecutePreviousMove()) {

            ((PlaybackLogic) logic).getPreviousMove();
            logic.executeMove();
            extraInput.setExecutePreviousMove(false);

            hud2d.removeLastAppendTeamInfo(logic.getNextTeamInTurn().getId());
            if (hud2d.getTeamInfo(logic.getNextTeamInTurn().getId()).length() == 0) {
                for (Team team : logic.getTeams()) {
                    if (logic.getNextTeamInTurn().equals(team)) {
                        hud2d.setTeamInfo(team.getId(), "Rolled: " + logic.getDieRoll());
                    } else {
                        hud2d.setTeamInfo(team.getId(), "");
                    }
                }
            }
            updateDieRoll();
        }
    }

    private void updateDieRoll() {
        if (!logic.isGameOver()) {

            die.setDieRoll(logic.getDieRoll());
            dieHolderDome.bounce();

            executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }
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

        if (showTags) {
            for (PieceGraphic p : pieces) {
                String tag = "[" + p.getPieceLogic().getId() + "]";
                font.renderString(textShader,
                        camera,
                        tag,
                        new Vector3f(p.getPosition().x, 1.5f, p.getPosition().z),
                        new Vector3f(camera.getRotation().x, (float) (-camera.getRotation().y + Math.PI), 0));
            }
        }

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

    public void toggleTags() {
        showTags = !showTags;
    }

    public boolean isShowTags() {
        return showTags;
    }

    public void toggleMoveAuto() {
        moveAuto = !moveAuto;
    }

    public boolean isMoveAuto() {
        return moveAuto;
    }

    private float goalAngle = 0;

    private void updateCameraAngle(float dt) {
        cameraPositionAngle = MathHelper.lerp(cameraPositionAngle, goalAngle, dt * 5);
    }

    public void rotateCameraToTeam(int teamID) {
        goalAngle = -board.getGoalSquares().get(logic.getBoard().getGoalSquare(teamID, 0).getID()).getRotation().y;

        if (goalAngle < 0) {
            goalAngle += 2 * Math.PI;
        } else if (goalAngle > 2 * Math.PI) {
            goalAngle -= 2 * Math.PI;
        }

        if (cameraPositionAngle > 2 * Math.PI) {
            cameraPositionAngle -= 2 * Math.PI;
        } else if (cameraPositionAngle < 0) {
            cameraPositionAngle += 2 * Math.PI;
        }

        float tempGoalAngle = (float) (goalAngle > cameraPositionAngle ? goalAngle : goalAngle + 2 * Math.PI);
        float diff = tempGoalAngle - cameraPositionAngle;
        if (diff > Math.PI) {
            goalAngle = (float) (tempGoalAngle - 2 * Math.PI);
        } else {
            goalAngle = tempGoalAngle;
        }
    }
}
