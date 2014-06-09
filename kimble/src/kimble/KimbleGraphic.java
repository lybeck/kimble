package kimble;

import java.awt.Font;
import java.io.IOException;
import kimble.logic.KimbleLogicInterface;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.playback.PlaybackProfile;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.input.ExtraInput;
import kimble.graphic.input.Input3D;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.Hud2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import kimble.logic.Team;
import kimble.playback.PlaybackLogic;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class KimbleGraphic extends AbstractKimbleGraphic {

    private BitmapFont font;
    private Hud2D hud;

    private Shader shader;
    private Shader textShader;

    private Input3D input;
    protected ExtraInput extraInput;

    private int lastTeamID = -1;

    private boolean showTags = false;
    private boolean moveAuto = true;

    private boolean endMessageShown = false;

    private boolean started;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        super(logic);
        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {
        super.setup();

        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        input = new Input3D(getCamera());
        extraInput = new ExtraInput(this);

        hud = new Hud2D(this, getLogic().getTeams());
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
        extraInput.update(dt);
        input.update(dt);
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
                // TODO: Make this method a manual one as well! (Needed if people wants to "believe that they rolled the die)
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
            }
        }
    }

    public void executeNextMove() {
        if (started) {
            executeMoveLogic();
            updateTeamInfo(getLogic().getNextTeamInTurn().getId(), getLogic().getDieRoll());
            updateDieRoll();
        }
    }

    private void executeMoveLogic() {
        if (executeMove) {
            getLogic().executeMove();
            executeMove = false;
        }
    }

    private void updateDieRoll() {
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

    public void updateTeamInfo(int teamID, int dieRoll) {

        for (Team team : getLogic().getTeams()) {
            if (getLogic().isFinished(team.getId())) {
                for (int i = 0; i < getLogic().getFinishedTeams().size(); i++) {
                    Team finishedTeam = getLogic().getFinishedTeams().get(i);
                    hud.setTeamInfo(finishedTeam.getId(), "Finished: " + (i + 1));
                    break;
                }
            } else if (getLogic().isDisqualified(team.getId())) {
                hud.setTeamInfo(team.getId(), "DSQ");
            } else if (team.getId() == teamID) {
                // Appends all the die rolls after each other on the hud.
                if (lastTeamID != teamID) {
                    hud.setTeamInfo(teamID, "Rolled: " + dieRoll);
                } else {
                    hud.appendTeamInfo(teamID, ", " + dieRoll);
                }
            } else {
                hud.setTeamInfo(team.getId(), "");
            }
        }
        lastTeamID = teamID;
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
