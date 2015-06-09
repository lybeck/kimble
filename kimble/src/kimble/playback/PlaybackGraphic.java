package kimble.playback;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.KimbleGraphic;
import kimble.graphic.input.Input3D;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.PlaybackHud;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.input.PlaybackInput;
import kimble.graphic.shader.Shader;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class PlaybackGraphic extends KimbleGraphic {

    private BitmapFont font;
    private PlaybackHud hud;

    private Shader shader;
    private Shader textShader;

    private Input3D input;
    private PlaybackInput extraInput;

    private int lastTeamID = -1;

    private boolean showTags = false;
    private boolean moveAuto = true;

    private boolean endMessageShown = false;

    private boolean started;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;

    public PlaybackGraphic(PlaybackLogic logic, PlaybackProfile profile) {
        super(logic, profile);
    }

    @Override
    public void setup() {
        super.setup();

        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        input = new Input3D(getCamera(), this);
        extraInput = new PlaybackInput(this);

        hud = new PlaybackHud(this, getLogic().getTeams());
        super.setHud(hud);
        try {
            font = FontGenerator.create("pieceLabel", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1), -0.02f);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGraphic.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load font for piece text!");
        }

    }

    @Override
    public void input(float dt) {
        input.update(dt);
        extraInput.update(dt);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (getLogic().isGameOver()) {
            for (int i = 0; i < getLogic().getFinishedTeams().size(); i++) {
                Team finishedTeam = getLogic().getFinishedTeams().get(i);
                hud.setTeamInfo(finishedTeam.getId(), "Finished " + (i + 1));
            }
//            stop();
        }

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
            if (!started) {
                updateStartingDieRoll(dt);
            }
        }

        hud.setPlaybackSpeed(PlaybackProfile.currentProfile);
        hud.update(dt);
    }

    private void updateExecuteMove(float dt) {

        executeMoveLogic();

        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!getLogic().isGameOver()) {
                updateTeamInfo(getLogic().getNextTeamInTurn().getId(), getLogic().getDieRoll());

                updateDieRoll();

                turnTimer = 0;
                nextTurnTimer = 0;
                executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }

                ((PlaybackLogic) getLogic()).getNextMove();
            }
        }
    }

    private void executeMoveLogic() {
        if (executeMove) {
            getLogic().executeMove();
            executeMove = false;
        }
    }

    public void executeMoveForward() {
        if (started) {
            executeMoveLogic();
//            logic.executeMove();
            ((PlaybackLogic) getLogic()).getNextMove();

            updateTeamInfo(getLogic().getNextTeamInTurn().getId(), getLogic().getDieRoll());
            updateDieRoll();
        }
    }

    public void executeMoveBackward() {
        if (started) {
            ((PlaybackLogic) getLogic()).getPreviousMove();
//            logic.executeMove();
            executeMoveLogic();

            hud.removeLastAppendTeamInfo(getLogic().getNextTeamInTurn().getId());
            if (hud.getTeamInfo(getLogic().getNextTeamInTurn().getId()).length() == 0) {
                for (Team team : getLogic().getTeams()) {
                    if (getLogic().getNextTeamInTurn().equals(team)) {
                        hud.setTeamInfo(team.getId(), "Rolled: " + getLogic().getDieRoll());
                    } else {
                        hud.setTeamInfo(team.getId(), "");
                    }
                }
            }
            updateDieRoll();
        }
    }

    @Override
    public void updateDieRoll() {
        if (!getLogic().isGameOver()) {

            die.setDieRoll(getLogic().getDieRoll());
            dieHolderDome.bounce();

            executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }

            hud.setTurnCount(getLogic().getTurnCount());
        }
    }

    private void updateStartingDieRoll(float dt) {
        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
            nextTurnTimer = 0;

            if (startingRollMapKeyIterator.hasNext()) {
                int teamID = startingRollMapKeyIterator.next();

                String oldMessage = hud.getTeamInfo(teamID);
                if (oldMessage == null || oldMessage.length() == 0) {
                    hud.setTeamInfo(teamID, "Rolled: " + startingRollMap.get(teamID));
                } else {
                    hud.appendTeamInfo(teamID, ", " + startingRollMap.get(teamID));
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

    @Override
    public void render() {
        shader.bind();
        renderComponents(shader);

        textShader.bind();
        if (showTags) {
            for (PieceGraphic p : pieces) {
                String tag = "[" + p.getPieceLogic().getId() + "]";
                font.renderString(textShader,
                        getCamera(),
                        tag,
                        new Vector3f(p.getPosition().x, 1.5f, p.getPosition().z),
                        new Vector3f(getCamera().getRotation().x, (float) (-getCamera().getRotation().y + Math.PI), 0));
            }
        }
        hud.render(textShader);
    }

    @Override
    public void dispose() {
        shader.dispose();
        textShader.dispose();

        super.dispose();
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

}
