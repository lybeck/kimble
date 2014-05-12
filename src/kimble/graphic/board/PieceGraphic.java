/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.logic.Piece;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class PieceGraphic extends Model {

    private final Piece pieceLogic;
    private final BoardGraphic board;

    public PieceGraphic(BoardGraphic board, Piece pieceLogic, Vector3f position, Vector3f color) {
        super(position, new Vector3f(0, 0, 0));

        this.board = board;
        this.pieceLogic = pieceLogic;

        this.getMaterial().setDiffuse(new Vector4f(color.x, color.y, color.z, 1));
        this.setMesh(ModelManager.getModel("game_piece"));
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
            setPosition(currentSquare.getPosition());
            rotate(0, currentSquare.getRotation().y, 0);
        } else {
            currentSquare = board.getEmptyHomeSquare(pieceLogic.getId(), pieceLogic.getTeamId());
            setPosition(currentSquare.getPosition());
            rotate(0, currentSquare.getRotation().y, 0);
        }
    }
}
