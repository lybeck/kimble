/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import java.io.File;
import kimble.graphic.VertexData;
import kimble.graphic.Model;
import kimble.graphic.loading.OBJLoader;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class SquareGraphic extends Model {

    private final float sideLength;
    private Vector3f color;

    public SquareGraphic(Vector3f position, float sideLength, Vector3f color) {
        super(position, new Vector3f(0, 0, 0));

//        OBJLoader.load(this.getClass().getResource("/res/models/game_board_positions.obj").getFile());

        this.sideLength = sideLength;
        this.color = color;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {
        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(-sideLength / 2, 0, sideLength / 2));
        v0.setColor(color);
        v0.setTexCoords(new Vector2f(0, 0));

        VertexData v1 = new VertexData();
        v1.setPosition(new Vector3f(-sideLength / 2, 0, -sideLength / 2));
        v1.setColor(color);
        v1.setTexCoords(new Vector2f(0, 1));

        VertexData v2 = new VertexData();
        v2.setPosition(new Vector3f(sideLength / 2, 0, -sideLength / 2));
        v2.setColor(color);
        v2.setTexCoords(new Vector2f(1, 1));

        VertexData v3 = new VertexData();
        v3.setPosition(new Vector3f(sideLength / 2, 0, sideLength / 2));
        v3.setColor(color);
        v3.setTexCoords(new Vector2f(1, 0));

        return new VertexData[]{v0, v1, v2, v3};
    }

    @Override
    public byte[] setupIndexData() {
        return new byte[]{0, 1, 2, 2, 3, 0};
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }
}
