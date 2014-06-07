package kimble.graphic.board;

import kimble.playback.PlaybackProfile;
import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.logic.Piece;
import kimble.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class PieceGraphic extends Model {

    private final Piece pieceLogic;
    private final BoardGraphic board;

    private Vector3f tempPosition;

    public PieceGraphic(BoardGraphic board, Piece pieceLogic, Vector3f position, Vector3f color) {
        super(position, new Vector3f(0, 0, 0));

        this.board = board;
        this.pieceLogic = pieceLogic;

        this.getMaterial().setDiffuse(new Vector4f(color.x, color.y, color.z, 1));
        this.setMesh(ModelManager.getModel("game_piece"));

        this.tempPosition = new Vector3f(getPosition().x, getPosition().y, getPosition().z);
    }

    float angle = 0;

    private boolean move(Vector3f currentSquarePosition) {
        return Math.abs(currentSquarePosition.x - getPosition().x) >= 0.01
                || Math.abs(currentSquarePosition.y - getPosition().y) >= 0.01
                || Math.abs(currentSquarePosition.z - getPosition().z) >= 0.01;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        SquareGraphic currentSquare;

        if (pieceLogic.getPosition() != null) {
            int squareID = pieceLogic.getPosition().getID();
            if (pieceLogic.getPosition().isGoalSquare()) {
                currentSquare = board.getGoalSquares().get(squareID);
            } else {
                currentSquare = board.getSquares().get(squareID);
            }

            if (move(currentSquare.getPosition())) {
                angle = MathHelper.lerp(angle, (float) Math.PI, dt * 10
                        * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());

                tempPosition.x = MathHelper.lerp(tempPosition.x, currentSquare.getPosition().x, dt * 10
                        * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());
                tempPosition.y = (float) (1.5 * Math.sin(angle)); //MathHelper.lerp(tempPosition.y, currentSquare.getPosition().y, dt * 10);
                tempPosition.z = MathHelper.lerp(tempPosition.z, currentSquare.getPosition().z, dt * 10
                        * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());
                setPosition(tempPosition);
                rotate(0, currentSquare.getRotation().y, 0);
            } else {
                angle = 0;
            }
        } else {
            currentSquare = board.getEmptyHomeSquare(pieceLogic.getId(), pieceLogic.getTeamId());
//            setPosition(currentSquare.getPosition());
            rotate(0, currentSquare.getRotation().y, 0);
            this.tempPosition = new Vector3f(getPosition().x, getPosition().y, getPosition().z);

            angle = 0;

            if (move(currentSquare.getPosition())) {
                angle = MathHelper.lerp(angle, (float) Math.PI, dt); // * 10 * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());

                tempPosition.x = MathHelper.lerp(tempPosition.x, currentSquare.getPosition().x, dt * 10
                        * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());
                tempPosition.y = (float) (1.5 * Math.sin(angle)); //MathHelper.lerp(tempPosition.y, currentSquare.getPosition().y, dt * 10);
                tempPosition.z = MathHelper.lerp(tempPosition.z, currentSquare.getPosition().z, dt * 10
                        * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());
                setPosition(tempPosition);
                rotate(0, currentSquare.getRotation().y, 0);
            } else {
                angle = 0;
            }
        }
    }

    public Piece getPieceLogic() {
        return pieceLogic;
    }

}
