package kimble;

import kimble.logic.KimbleLogicInterface;
import kimble.graphic.AbstractGraphic;
import kimble.playback.PlaybackProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class KimbleGraphic extends AbstractGraphic {

    private final KimbleLogicInterface logic;

    private HUD hud;
    private Team nextTeam;
    private Move selectedMove;
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

    private boolean started;

    private List<Map<Integer, Integer>> startingDieRolls;
    private Iterator<Map<Integer, Integer>> startingRollsIterator;
    private Map<Integer, Integer> startingRollMap;
    private Iterator<Integer> startingRollMapKeyIterator;

    private boolean executeMove = false;
    private float turnTimer = 0;
    private float nextTurnTimer = 0;
    private float cameraPositionAngle = 0;

    public KimbleGraphic(KimbleLogicInterface logic, PlaybackProfile profile, boolean useHud) {
        super(useHud);
        this.logic = logic;

        PlaybackProfile.setCurrentProfile(profile);
    }

    @Override
    public void setup() {
        if (useHud) {
            hud = new HUD();
        }

        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getBoard(), logic.getTeams(), new BoardSpecs(SQUARES_FROM_START_TO_START));
        shader = new Shader("shader.vert", "shader.frag");

        camera = new Camera3D(new Vector3f(2, 7, -2), new Vector3f((float) (Math.PI / 3.0), 0, 0), 70f, 0.3f, 1000f);
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

        if (useHud) {
            hud.appendLine("===============================");
            hud.appendLine("Rolling for starting order");
            hud.appendLine("===============================\n");
            started = false;
        }
    }

    @Override
    public void update(float dt) {
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
            if (useHud) {
                hud.setViewport(0, 0, Screen.getWidth(), Screen.getHeight());
            }
            camera.setupProjectionMatrix();
//            camera.updateProjectionMatrixAttributes();
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

        if (useHud) {
            hud.update(dt);
        }
    }

    private void updateExecuteMove(float dt) {

        if (executeMove) {
            logic.executeMove();
            executeMove = false;

            if (useHud) {
                selectedMove = logic.getSelectedMove();
                hud.appendLine(moveInfoText());
            }
        }

        nextTurnTimer += dt;
        if (nextTurnTimer >= PlaybackProfile.currentProfile.getTurnTimeStep()) {

            if (!logic.isGameOver()) {
                if (useHud) {
                    nextTeam = logic.getNextTeamInTurn();
                    hud.appendLine(dieRollInfoText());
                }

                die.setDieRoll(logic.getDieRoll());
                dieHolderDome.bounce();

                turnTimer = 0;
                nextTurnTimer = 0;
                executeMove = true;
            } else {
                if (!endMessageShown && useHud) {
                    hud.appendLine(finnishInfoText());
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

                if (useHud) {
                    hud.appendLine(new StringBuilder().append("[ID = ")
                            .append(teamID).append("] rolled ")
                            .append(dieRoll).append(" (")
                            .append(logic.getTeam(teamID).getName())
                            .append(")\n"));
                }

                die.setDieRoll(dieRoll);
                dieHolderDome.bounce();
            } else {
                if (startingRollsIterator.hasNext()) {
                    startingRollMap = startingRollsIterator.next();
                    startingRollMapKeyIterator = startingRollMap.keySet().iterator();
                    if (useHud) {
                        hud.appendLine("");
                    }
                } else {
                    if (useHud) {
                        Team startingTeam = logic.getStartingTeam();
                        hud.appendLine(new StringBuilder().append("\n[ID = ")
                                .append(startingTeam.getId())
                                .append("] starts the game (")
                                .append(startingTeam.getName())
                                .append(")\n"));
                        hud.appendLine("===============================");
                        hud.appendLine("");
                    }
                    started = true;
                }
            }
        }
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
        shader.unbind();

        if (useHud) {
            hud.render();
        }
    }

    @Override
    public void dispose() {

        shader.dispose();
        board.dispose();

        dieHolder.dispose();
        die.dispose();
        dieHolderDome.dispose();

        for (PieceGraphic p : pieces) {
            p.dispose();
        }

        if (useHud) {
            hud.dispose();
        }

        ModelManager.dispose();
        TextureManager.dispose();

        Screen.dispose();
    }

    // <editor-fold defaultstate="collapsed" desc="HUD information texts">
    /**
     * Creates a string builder for the Die Roll info text. For use in HUD.
     *
     * @return
     */
    private StringBuilder dieRollInfoText() {
        // Append a new die roll to the info-stream in gui
        StringBuilder sb = new StringBuilder();
        sb.append("[ID = ")
                .append(nextTeam.getId())
                .append("] rolled ")
                .append(logic.getDieRoll());
        return sb;
    }

    private StringBuilder moveInfoText() {
        // append the move info based on the move selection.
        StringBuilder sb = new StringBuilder();
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
        return sb;
    }

    private StringBuilder finnishInfoText() {
        StringBuilder sb = new StringBuilder();
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

        return sb;
    }
    //</editor-fold>
}
