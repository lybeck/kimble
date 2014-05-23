/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.hud;

import kimble.graphic.model.Mesh;
import kimble.graphic.model.VertexData;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class RectangleMesh extends Mesh {

    private float x;
    private float y;
    private float width;
    private float height;

    private Vector4f color;

    public RectangleMesh(float x, float y, float width, float height, Vector4f color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {

        float tx = 1 / 16.0f * 1;
        float ty = 1 / 16.0f * 4;

        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(x, y, 0));
        v0.setNormal(new Vector3f(0, 0, -1));
        v0.setColor(color);

        VertexData v1 = new VertexData();
        v1.setPosition(new Vector3f(x, y + height, 0));
        v1.setNormal(new Vector3f(0, 0, -1));
        v1.setColor(color);

        VertexData v2 = new VertexData();
        v2.setPosition(new Vector3f(x + width, y + height, 0));
        v2.setNormal(new Vector3f(0, 0, -1));
        v2.setColor(color);

        VertexData v3 = new VertexData();
        v3.setPosition(new Vector3f(x + width, y, 0));
        v3.setNormal(new Vector3f(0, 0, -1));
        v3.setColor(color);

        return new VertexData[]{v0, v1, v2, v3};
    }

    @Override
    public int[] setupIndexData() {
        return new int[]{0, 1, 2, 2, 3, 0};
    }

}
