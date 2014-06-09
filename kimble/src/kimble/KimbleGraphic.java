package kimble;

import java.awt.Font;
import java.io.IOException;
import kimble.logic.KimbleLogicInterface;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.playback.PlaybackProfile;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.Hud2D;
import kimble.graphic.hud.font.FontGenerator;
import kimble.logic.Team;
import kimble.playback.PlaybackLogic;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class KimbleGraphic extends AbstractKimbleGraphic {

    private Hud2D hud;
    private int lastTeamID = -1;

    private boolean showTags = false;
    private boolean moveAuto = true;

    private boolean endMessageShown = false;

    private boolean started;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;

//    private float cameraPositionAngle;
    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        super(logic);
        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {
        super.setup();

        hud = new Hud2D(this, logic.getTeams());
        super.setHud(hud);
        try {
            font = FontGenerator.create("pieceLabel", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1), -0.02f);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGraphic.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load font for piece text!");
        }

    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (logic.isGameOver()) {
            for (int i = 0; i < logic.getFinishedTeams().size(); i++) {
                Team finishedTeam = logic.getFinishedTeams().get(i);
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

        hud.setPlaybackSpeed(PlaybackProfile.currentProfile);
        hud.update(dt);
    }

    private void updateExecuteMove(float dt) {

        executeMoveLogic();

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

                if (logic instanceof PlaybackLogic) {
                    ((PlaybackLogic) logic).getNextMove();
                }
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

            executeMoveLogic();
//            logic.executeMove();
            ((PlaybackLogic) logic).getNextMove();
            extraInput.setExecuteNextMove(false);

            updateTeamInfo(logic.getNextTeamInTurn().getId(), logic.getDieRoll());
            updateDieRoll();

        } else if (extraInput.isExecutePreviousMove()) {

            ((PlaybackLogic) logic).getPreviousMove();
//            logic.executeMove();
            executeMoveLogic();
            extraInput.setExecutePreviousMove(false);

            hud.removeLastAppendTeamInfo(logic.getNextTeamInTurn().getId());
            if (hud.getTeamInfo(logic.getNextTeamInTurn().getId()).length() == 0) {
                for (Team team : logic.getTeams()) {
                    if (logic.getNextTeamInTurn().equals(team)) {
                        hud.setTeamInfo(team.getId(), "Rolled: " + logic.getDieRoll());
                    } else {
                        hud.setTeamInfo(team.getId(), "");
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

            hud.setTurnCount(logic.getTurnCount());
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

    private void updateTeamInfo(int teamID, int dieRoll) {

        for (Team team : logic.getTeams()) {
            if (logic.isFinished(team.getId())) {
                for (int i = 0; i < logic.getFinishedTeams().size(); i++) {
                    Team finishedTeam = logic.getFinishedTeams().get(i);
                    hud.setTeamInfo(finishedTeam.getId(), "Finished: " + (i + 1));
                    break;
                }
            } else if (logic.isDisqualified(team.getId())) {
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
        super.render();

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

        hud.render(textShader);
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
