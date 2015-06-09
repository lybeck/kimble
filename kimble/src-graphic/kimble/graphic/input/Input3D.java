package kimble.graphic.input;

import java.util.ArrayList;
import java.util.List;
import kimble.KimbleGraphic;
import kimble.graphic.camera.Camera3D;
import kimble.graphic.hud.KimbleHud;
import kimble.graphic.pickingray.Ray;
import kimble.graphic.pickingray.RayGenerator;
import kimble.logic.Constants;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Input3D extends Input {

    //---------------------------------------------------------------------
    // Graphic update stuff
    //---------------------------------------------------------------------
    private List<KimbleGraphic.AvailableMove> movablePieces;

    private final KimbleGraphic graphic;

    private KimbleGraphic.AvailableMove selectedAvailableMove;
    private KimbleGraphic.Destination dest;
    //---------------------------------------------------------------------

    private float angle;

    private boolean domeClicked = false;

    private float mouseSpeed;
    private float moveSpeed;

    public Input3D(Camera3D camera, KimbleGraphic graphic) {
        super(camera);

        this.graphic = graphic;

        this.mouseSpeed = Constants.DEFAULT_MOUSE_SPEED;
        this.moveSpeed = Constants.DEFAULT_MOVE_SPEED;

        this.movablePieces = new ArrayList<>();
        this.selectedAvailableMove = null;

        this.dest = null;

        this.angle = 0;
    }

    @Override
    public void inputMouse(float dt) {

        updateMousePicking();

        if (Mouse.isGrabbed()) {
            getCamera().getRotation().y += mouseSpeed * dt * Mouse.getDX();
            getCamera().getRotation().x -= mouseSpeed * dt * Mouse.getDY();

            getCamera().getRotation().x = Math.min(getCamera().getRotation().x, getCamera().getMaxYaw());
            getCamera().getRotation().x = Math.max(getCamera().getRotation().x, getCamera().getMinYaw());

            getCamera().getRotation().y %= 2 * Math.PI;
            getCamera().getRotation().x %= 2 * Math.PI;
        } else {
            if (selectedAvailableMove == null) {
                for (KimbleGraphic.AvailableMove move : movablePieces) {
                    move.piece.move(0, (float) (0.25 * Math.sin(angle)), 0);
                }
            } else {
                if (!(dest != null && selectedAvailableMove.destinationID == dest.id)) {
                    graphic.getBoard().getSquares().get(selectedAvailableMove.destinationID).move(0, (float) (0.1
                            * Math.sin(angle)), 0);
                }
            }

            angle += dt * 5;
            if (angle >= Math.PI) {
                angle = 0;
            }
        }
    }

    private void updateMousePicking() {

        // TODO: select pieces and drag them along the ground
        // TODO: click on a piece to make it execute it's move. Click the die to roll it! (this is possible when the game logic has been refined)
        // TODO: what kind of movement do we want? Click on piece, click on square -> animate piece jumping to square.
        if (Mouse.isButtonDown(0)) {
            Ray ray = RayGenerator.create(Mouse.getX(), Mouse.getY(), getCamera());

            if (domeClicked == false && selectedAvailableMove == null && ray.intersects(graphic.getDieHolderDome())) {
                graphic.updateDieRoll();
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
                    for (KimbleGraphic.AvailableMove move : movablePieces) {
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
                if (graphic.onlyOptionalMoves()) {
                    ((KimbleHud) graphic.getHud()).getPassTurnButton().setEnabled(true);
                }
                graphic.tryExecuteNextMove();

                angle = 0;
                domeClicked = false;
            } else {
                if (selectedAvailableMove != null) {
                    // TODO: Come back and look at this piece of code
                    if (dest != null) {
                        graphic.movePieceToDestination(selectedAvailableMove.piece.getPieceLogic(), dest.id);
                        selectedAvailableMove.piece.setSelected(false);
                        selectedAvailableMove = null;
                    }
                }
            }
        }
    }

    private KimbleGraphic.Destination testPiecePosition(Ray ray) {
        KimbleGraphic.Destination dest = graphic.new Destination();
        for (Integer key : graphic.getBoard().getSquares().keySet()) {
            if (ray.intersects(graphic.getBoard().getSquares().get(key))) {
                if (graphic.getBoard().getSquares().get(key).getLogic() != null) {
                    /*
                     * marks the hovered square
                     */
//                    dest.position = board.getSquares().get(key).getPosition();
//                    dest.position.y += 0.1f;
                    /*
                     * marks the hovered square
                     */
                    /*
                     * small lift to the piece when at right square
                     */
                    dest.position = new Vector3f(graphic.getBoard().getSquares().get(key).getPosition());
                    dest.position.y += 0.1f;
                    /*
                     * small lift to the piece when at right square
                     */
                    dest.id = graphic.getBoard().getSquares().get(key).getLogic().getID();
                    break;
                }
            }
        }
        return dest;
    }

    @Override
    public void inputKeyboard(float dt) {
        if (Mouse.isGrabbed()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                getCamera().move(0, 0, -moveSpeed * dt);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                getCamera().move(0, 0, moveSpeed * dt);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                getCamera().move(-moveSpeed * dt, 0, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                getCamera().move(moveSpeed * dt, 0, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                getCamera().move(0, moveSpeed * dt, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                getCamera().move(0, -moveSpeed * dt, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Mouse.setGrabbed(false);
            }
        }
    }

    @Override
    public Camera3D getCamera() {
        return (Camera3D) super.getCamera();
    }

    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }

    public float getMouseSpeed() {
        return mouseSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    //-----------------------------------------------------------
    //
    //-----------------------------------------------------------
    public void setMovablePieces(List<KimbleGraphic.AvailableMove> movablePieces) {
        this.movablePieces = movablePieces;
    }

}
