package kimble;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.graphic.Model;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.board.TableGraphic;
import kimble.graphic.hud.KimbleHud;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.input.ExtraInput;
import kimble.graphic.input.Input3D;
import kimble.graphic.shader.Shader;
import kimble.logic.KimbleLogicInterface;
import kimble.logic.Move;
import kimble.logic.Piece;
import kimble.logic.Team;
import kimble.logic.player.KimblePlayer;
import kimble.playback.PlaybackProfile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class KimbleGraphic extends AbstractKimbleGraphic {

    private BitmapFont font;
    private KimbleHud hud;

    private Shader shader;
    private Shader textShader;

    private Input3D input;
    protected ExtraInput extraInput;

    private int lastTeamID = -1;

    private boolean showTags = false;
    private boolean moveAuto = true;

    private boolean started;
    private boolean dieRolled = false;
    private boolean executeMove = false;
    private boolean executeNextMove;

    private boolean endMessageShown = false;

    private float turnTimer = 0;
    private float nextTurnTimer = 0;

    private Model table;

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile) {
        super(logic);
        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {
        super.setup();

        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        input = new Input3D(getCamera(), this);
        extraInput = new ExtraInput(this);

        hud = new KimbleHud(this, getLogic().getTeams());
        hud.getPassTurnButton().setEnabled(false);
        super.setHud(hud);

        try {
            font = FontGenerator.create("pieceLabel", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1), -0.02f);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGraphic.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load font for piece text!");
        }

        table = new TableGraphic();
    }

    @Override
    public void input(float dt) {
        extraInput.update(dt);
        input.update(dt);
        hud.update(dt);

        // empty the Mouse to not interfere with the HUD.
        while (Mouse.next()) {
            Mouse.poll();
        }
        // empty the Keyboard to not interfere with the HUD.
        while (Keyboard.next()) {
            Keyboard.poll();
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // If the game is over show the finishing order and the winner.
        if (getLogic().isGameOver()) {
            for (int i = 0; i < getLogic().getFinishedTeams().size(); i++) {
                Team finishedTeam = getLogic().getFinishedTeams().get(i);
                hud.setTeamInfo(finishedTeam.getId(), "Finished " + (i + 1));
            }
//            stop();
        }

        // Check whether the player is a human
        if (getLogic().isAutoPlayer()) {
            hud.getPassTurnButton().setEnabled(false);

            turnTimer += dt;
            if (turnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {
                if (started) {
                    if (moveAuto) {
                        updateExecuteMove(dt);
                    } else {
                        if (executeNextMove) {
                            updateExecuteMove(dt);
                            executeNextMove = false;
                        }
                    }
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

        table.update(dt);
    }

    /**
     * Highlights all movable pieces. If no piece is movable, the turn will be given to the next player.
     */
    private void highlightMovablePieces() {
        input.cleaMovablePieces();

        if (getLogic().getCurrentTurn().getMoves().isEmpty()) {
            System.out.println("no move available, continue with next team.");
            executeMoveLogic();
        } else {
            // Assigning the current movable pieces to the list. Checking the move data given by the logic.
            for (PieceGraphic p : pieces) {
                for (Move move : getLogic().getCurrentTurn().getMoves()) {
                    if (p.getPieceLogic().equals(move.getPiece())) {
                        AvailableMove availableMove = new AvailableMove();
                        availableMove.destinationID = move.getDestination().getID();
                        availableMove.piece = p;
                        input.addMovablePiece(availableMove);
                        break;
                    }
                }
            }
        }
    }

    public void checkOnlyOptionalMoves() {
        if (getLogic().getCurrentTurn().getMoves().isEmpty()) {
            return;
        }

        for (Move move : getLogic().getCurrentTurn().getMoves()) {
            if (!move.isOptional()) {
                return;
            }
        }

        // If all moves are optional, one can choose to pass the turn.
        hud.getPassTurnButton().setEnabled(true);
    }

    private void updateExecuteMove(float dt) {

        executeMoveLogic();

        // continues with the timer to handle all the updates
        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!getLogic().isGameOver()) {
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

    /**
     * Try to execute a move, if it is possible the die will do some roll magic and the game highlights the movable
     * pieces for the player. If it is a bot in turn the game will just continue with the next player.
     */
    public void tryExecuteNextMove() {
        if (started) {
            updateDieRoll();
            executeNextMove = true;

            if (!getLogic().isAutoPlayer()) {
                highlightMovablePieces();
            }
        } else {
            System.out.println("cheater!!! You can't make a move before the game has started");
        }
    }

    /**
     * Perform the logical move if it is allowed to do it.
     *
     * The method doesn't do anything if the <code>executeMove</code> variable is false. Otherwise it will tell the
     * logic to execute the next move and assign the <code>dieRolled</code> and <code>executeMove</code> variables to
     * false and wait for the user interface to change the state of these variables again.
     */
    private void executeMoveLogic() {
        if (executeMove) {
            getLogic().executeMove();
            executeMove = false;
            dieRolled = false;
        }
    }

    public void updateDieRoll() {
        if (!getLogic().isGameOver() && !dieRolled) {
            updateTeamInfo(getLogic().getNextTeamInTurn().getId(), getLogic().getDieRoll());

            die.setDieRoll(getLogic().getDieRoll(), PlaybackProfile.currentProfile.getTurnTimeStep());
            dieHolderDome.bounce();
            dieRolled = true;

            executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }

            input.cleaMovablePieces();

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

                die.setDieRoll(startingRollMap.get(teamID), PlaybackProfile.currentProfile.getTurnTimeStep());
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

        table.render(shader, getCamera());

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

        table.dispose();

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

    public void movePieceToDestination(Piece pieceLogic, int destinationId) {
        if (((KimblePlayer) getLogic().getCurrentPlayer()).selectMove(pieceLogic, destinationId, getLogic().getCurrentTurn())) {
            executeMoveLogic();
            input.cleaMovablePieces();
        }
    }

    /**
     * Make sure to check if there are only optional moves in the available moves list. See @checkOnlyOptionalMoves()
     */
    public void passTurnIfOnlyOptionalMoves() {
        if (!getLogic().isAutoPlayer()) {
            ((KimblePlayer) getLogic().getCurrentPlayer()).passTurn();
            executeMoveLogic();

            input.cleaMovablePieces();
            hud.getPassTurnButton().setEnabled(false);
        }
    }
    // ===================================================
    /*
     * Classes helping the Mouse picking
     */

    // ===================================================
    public class Destination {

        public Vector3f position;
        public int id;
    }

    public class AvailableMove {

        public PieceGraphic piece;
        public int destinationID;
    }
}
