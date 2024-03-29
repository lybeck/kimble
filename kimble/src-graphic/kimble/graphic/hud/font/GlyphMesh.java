package kimble.graphic.hud.font;

import kimble.graphic.model.Mesh;
import kimble.graphic.model.VertexData;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class GlyphMesh extends Mesh {

    private final float tx;
    private final float ty;
    private final float cellWidth;
    private final float cellHeight;
    private final float width;
    private final float height;
    private final Vector4f color;

    public GlyphMesh(float tx, float ty, float cellWidth, float cellHeight, float width, float height, Vector4f color) {
        this.tx = tx;
        this.ty = ty;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.width = width;
        this.height = height;
        this.color = color;
        setup();
    }

    @Override
    public VertexData[] setupVertexData() {
        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(0, 0, 0));
        v0.setNormal(new Vector3f(0, 0, -1));
        v0.setTexCoords(new Vector2f(tx, ty));
        v0.setColor(color);

        VertexData v1 = new VertexData();
        v1.setPosition(new Vector3f(0, height, 0));
        v1.setNormal(new Vector3f(0, 0, -1));
        v1.setTexCoords(new Vector2f(tx, ty + cellHeight));
        v1.setColor(color);

        VertexData v2 = new VertexData();
        v2.setPosition(new Vector3f(width, height, 0));
        v2.setNormal(new Vector3f(0, 0, -1));
        v2.setTexCoords(new Vector2f(tx + cellWidth, ty + cellHeight));
        v2.setColor(color);

        VertexData v3 = new VertexData();
        v3.setPosition(new Vector3f(width, 0, 0));
        v3.setNormal(new Vector3f(0, 0, -1));
        v3.setTexCoords(new Vector2f(tx + cellWidth, ty));
        v3.setColor(color);

        return new VertexData[]{v0, v1, v2, v3};
    }

    @Override
    public int[] setupIndexData() {
        return new int[]{0, 1, 2, 2, 3, 0};
    }
}
