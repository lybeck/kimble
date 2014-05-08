/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.VertexData;
import kimble.graphic.loading.OBJLoader;
import kimble.logic.Piece;
import kimble.logic.board.Square;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class PieceGraphic extends Model {

    private Vector3f color;
    private final float width;
    private final float height;

    private final Piece pieceLogic;
    private final BoardGraphic board;

    public PieceGraphic(BoardGraphic board, Piece pieceLogic, Vector3f position, Vector3f color, float width, float height) {
        super(position, new Vector3f(0, 0, 0));

        this.board = board;
        this.pieceLogic = pieceLogic;

        this.width = width;
        this.height = height;

        this.color = color;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {
        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(-width / 2, 1, width / 2));
        v0.setColor(color);
        v0.setTexCoords(new Vector2f(0, 0));

        VertexData v1 = new VertexData();
        v1.setPosition(new Vector3f(-width / 2, 1, -width / 2));
        v1.setColor(color);
        v1.setTexCoords(new Vector2f(0, 1));

        VertexData v2 = new VertexData();
        v2.setPosition(new Vector3f(width / 2, 1, -width / 2));
        v2.setColor(color);
        v2.setTexCoords(new Vector2f(1, 1));

        VertexData v3 = new VertexData();
        v3.setPosition(new Vector3f(width / 2, 1, width / 2));
        v3.setColor(color);
        v3.setTexCoords(new Vector2f(1, 0));

        VertexData v4 = new VertexData();
        v4.setPosition(new Vector3f(-width / 2, height, width / 2));
        v4.setColor(color);
        v4.setTexCoords(new Vector2f(0, 0));

        VertexData v5 = new VertexData();
        v5.setPosition(new Vector3f(-width / 2, height, -width / 2));
        v5.setColor(color);
        v5.setTexCoords(new Vector2f(0, 1));

        VertexData v6 = new VertexData();
        v6.setPosition(new Vector3f(width / 2, height, -width / 2));
        v6.setColor(color);
        v6.setTexCoords(new Vector2f(1, 1));

        VertexData v7 = new VertexData();
        v7.setPosition(new Vector3f(width / 2, height, width / 2));
        v7.setColor(color);
        v7.setTexCoords(new Vector2f(1, 0));

        return new VertexData[]{v0, v1, v2, v3, v4, v5, v6, v7};
    }

    @Override
    public byte[] setupIndexData() {
        return new byte[]{0, 1, 2, 2, 3, 0,
            0, 1, 5, 5, 4, 0,
            1, 2, 6, 6, 5, 1,
            2, 3, 7, 7, 6, 2,
            3, 0, 4, 4, 7, 3,
            4, 5, 6, 6, 7, 5};
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        SquareGraphic currentSquare = null;

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

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
