/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.VertexData;
import kimble.graphic.loading.Mesh;
import kimble.logic.Die;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class DieGraphic extends Model {

    private final Die die;
    private final float width;

    private Vector3f color = new Vector3f(0.8f, 0.8f, 0.8f);

    public DieGraphic(Die die, float width) {

        this.die = die;
        this.width = width;

        this.setMesh(new DieMesh(width, new Vector3f(0.8f, 0.8f, 0.8f)));
    }

    private static class DieMesh extends Mesh {

        private float width;
        private Vector3f color;

        public DieMesh(float width, Vector3f color) {
            this.width = width;
            this.color = color;

            setup();
        }

        @Override
        public VertexData[] setupVertexData() {
            VertexData v0 = new VertexData();
            v0.setPosition(new Vector3f(-width / 2, 0, width / 2));
            v0.setColor(color);
            v0.setTexCoords(new Vector2f(0, 0));

            VertexData v1 = new VertexData();
            v1.setPosition(new Vector3f(-width / 2, 0, -width / 2));
            v1.setColor(color);
            v1.setTexCoords(new Vector2f(0, 1));

            VertexData v2 = new VertexData();
            v2.setPosition(new Vector3f(width / 2, 0, -width / 2));
            v2.setColor(color);
            v2.setTexCoords(new Vector2f(1, 1));

            VertexData v3 = new VertexData();
            v3.setPosition(new Vector3f(width / 2, 0, width / 2));
            v3.setColor(color);
            v3.setTexCoords(new Vector2f(1, 0));

            VertexData v4 = new VertexData();
            v4.setPosition(new Vector3f(-width / 2, width, width / 2));
            v4.setColor(color);
            v4.setTexCoords(new Vector2f(0, 0));

            VertexData v5 = new VertexData();
            v5.setPosition(new Vector3f(-width / 2, width, -width / 2));
            v5.setColor(color);
            v5.setTexCoords(new Vector2f(0, 1));

            VertexData v6 = new VertexData();
            v6.setPosition(new Vector3f(width / 2, width, -width / 2));
            v6.setColor(color);
            v6.setTexCoords(new Vector2f(1, 1));

            VertexData v7 = new VertexData();
            v7.setPosition(new Vector3f(width / 2, width, width / 2));
            v7.setColor(color);
            v7.setTexCoords(new Vector2f(1, 0));

            return new VertexData[]{v0, v1, v2, v3, v4, v5, v6, v7};
        }

        @Override
        public int[] setupIndexData() {
            return new int[]{0, 1, 2, 2, 3, 0,
                0, 1, 5, 5, 4, 0,
                1, 2, 6, 6, 5, 1,
                2, 3, 7, 7, 6, 2,
                3, 0, 4, 4, 7, 3,
                4, 5, 6, 6, 7, 5};
        }
    }

}
