package kimble;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.hud.KimbleHud;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.input.ExtraInput;
import kimble.graphic.input.Input3D;
import kimble.graphic.pickingray.Ray;
import kimble.graphic.pickingray.RayGenerator;
import kimble.graphic.shader.Shader;
import kimble.logic.KimbleLogicInterface;
import kimble.logic.Move;
import kimble.logic.Team;
import kimble.logic.player.KimblePlayer;
import kimble.playback.PlaybackProfile;
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

    private boolean endMessageShown = false;

    private boolean started;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;

    private AvailableMove selectedAvailableMove = null;
    private Destination dest = null;
    private final List<AvailableMove> movablePieces = new ArrayList<>();
    private boolean domeClicked = false;
    private boolean dieRolled = false;

    private float angle = 0;
    private boolean executeNextMove;

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

        hud = new KimbleHud(this, getLogic().getTeams());
        hud.getPassTurnButton().setEnabled(false);
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

            if (started) {
                if (selectedAvailableMove == null) {
                    for (AvailableMove move : movablePieces) {
                        move.piece.move(0, (float) (0.25 * Math.sin(angle)), 0);
                    }
                } else {
                    if (!(dest != null && selectedAvailableMove.destinationID == dest.id)) {
                        board.getSquares().get(selectedAvailableMove.destinationID).move(0, (float) (0.1
                                * Math.sin(angle)), 0);
                    }
                }

                angle += dt * 5;
                if (angle >= Math.PI) {
                    angle = 0;
                }
            } else {
                // TODO: Make this method a manual one as well! (Needed if people wants to "believe that they rolled the die)
                updateStartingDieRoll(dt);
            }
        }

        hud.setPlaybackSpeed(PlaybackProfile.currentProfile);
        hud.update(dt);

        updateMousePicking();
    }

    private void updateMousePicking() {

        // TODO: select pieces and drag them along the ground
        // TODO: click on a piece to make it execute it's move. Click the die to roll it! (this is possible when the game logic has been refined)
        // TODO: what kind of movement do we want? Click on piece, click on square -> animate piece jumping to square.
        if (Mouse.isButtonDown(0)) {
            Ray ray = RayGenerator.create(Mouse.getX(), Mouse.getY(), getCamera());

            if (domeClicked == false && selectedAvailableMove == null && ray.intersects(dieHolderDome)) {
                updateDieRoll();
                domeClicked = true;
            } else {

                if (selectedAvailableMove != null) {
                    dest = testPiecePosition(ray);
                } else {
                    dest = null;
                }

                if (selectedAvailableMove != null) {
                    if (dest.position == null || dest.id != selectedAvailableMove.destinationID) {
                        selectedAvailableMove.piece.setSelectedPosition(ray.getIntersectPointAtHeight(1f));
                    } else {
                        selectedAvailableMove.piece.setSelectedPosition(new Vector3f(dest.position));
                    }
                } else {
                    for (AvailableMove move : movablePieces) {
                        if (ray.intersects(move.piece)) {
                            selectedAvailableMove = move;
                            selectedAvailableMove.piece.setSelected(true);
                            break;
                        }
                    }
                }
            }
        } else {
            if (domeClicked) {

                if (onlyOptionalMoves()) {
                    hud.getPassTurnButton().setEnabled(true);
                }

                tryExecuteNextMove();

                angle = 0;
                domeClicked = false;
            } else {

                if (selectedAvailableMove != null) {

                    if (!getLogic().isAutoPlayer() && dest != null) {
                        if (((KimblePlayer) getLogic().getCurrentPlayer()).selectMove(selectedAvailableMove.piece.getPieceLogic(), dest.id, getLogic().getCurrentTurn())) {
                            executeMoveLogic();
                            movablePieces.clear();
                        }
                        selectedAvailableMove.piece.setSelected(false);
                        selectedAvailableMove = null;
                    }
                }
            }
        }
        // empty the Mouse to not interfere with the HUD.
        while (Mouse.next()) {
            Mouse.poll();
        }

    }

    /**
     * Highlights all movable pieces. If no piece is movable, the turn will be given to the next player.
     */
    private void highlightMovablePieces() {
        if (getLogic().getCurrentTurn().getMoves().isEmpty()) {
            System.out.println("no move available, continue with next team.");
            executeMoveLogic();
        } else {
            movablePieces.clear();
            for (PieceGraphic p : pieces) {
                for (Move move : getLogic().getCurrentTurn().getMoves()) {
                    if (p.getPieceLogic().equals(move.getPiece())) {
                        AvailableMove availableMove = new AvailableMove();
                        availableMove.destinationID = move.getDestination().getID();
                        availableMove.piece = p;
                        movablePieces.add(availableMove);
                        break;
                    }
                }
            }
        }
    }

    public boolean onlyOptionalMoves() {
        if (getLogic().getCurrentTurn().getMoves().isEmpty()) {
            return false;
        }

        for (int i = 0; i < getLogic().getCurrentTurn().getMoves().size(); i++) {
            if (!getLogic().getCurrentTurn().getMoves().get(i).isOptional()) {
                return false;
            }
        }
        return true;
    }

    private Destination testPiecePosition(Ray ray) {
        Destination dest = new Destination();
        for (Integer key : board.getSquares().keySet()) {
            if (ray.intersects(board.getSquares().get(key))) {
                if (board.getSquares().get(key).getLogic() != null) {
                    dest.position = board.getSquares().get(key).getPosition();
                    dest.id = board.getSquares().get(key).getLogic().getID();
                    break;
                }
            }
        }
        return dest;
    }

    private void updateExecuteMove(float dt) {

        executeMoveLogic();

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

    public void tryExecuteNextMove() {
        if (started) {
            updateDieRoll();
            executeNextMove = true;

            if (!getLogic().isAutoPlayer()) {
                highlightMovablePieces();
            }
        }
    }

    private void executeMoveLogic() {
        if (executeMove) {
            getLogic().executeMove();
            executeMove = false;
            dieRolled = false;
        }
    }

    private void updateDieRoll() {
        if (!getLogic().isGameOver() && !dieRolled) {
            updateTeamInfo(getLogic().getNextTeamInTurn().getId(), getLogic().getDieRoll());

            die.setDieRoll(getLogic().getDieRoll());
            dieHolderDome.bounce();
            dieRolled = true;

            executeMove = true;
//            } else {
//                if (!endMessageShown) {
//                    endMessageShown = true;
//                }

            movablePieces.clear();

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

    /**
     * Make sure to check if there are only optional moves in the available moves list. See @onlyOptionalMoves()
     */
    public void passTurnIfOnlyOptionalMoves() {
        if (!getLogic().isAutoPlayer()) {
            ((KimblePlayer) getLogic().getCurrentPlayer()).passTurn();
            executeMoveLogic();

            movablePieces.clear();
            hud.getPassTurnButton().setEnabled(false);
        }
    }
    // ===================================================
    /*
     * Classes helping the Mouse picking
     */

    // ===================================================
    private class Destination {

        public Vector3f position;
        public int id;
    }

    private class AvailableMove {

        public PieceGraphic piece;
        public int destinationID;
    }
}
