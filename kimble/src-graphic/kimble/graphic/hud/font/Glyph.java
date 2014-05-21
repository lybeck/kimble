package kimble.graphic.hud.font;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Glyph extends Model {

    private float width;
    private float height;

    public Glyph(float tx, float ty, float cellWidth, float cellHeight, float width, float height) {
        this.getMaterial().setDiffuse(new Vector4f(1, 1, 1, 1));
        this.getMaterial().setTextureModulator(1.0f);
        this.setScale(1, 1, 0);
        this.setMesh(new GlyphMesh(tx, ty, cellWidth, cellHeight, width, height));

        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
