package kimble.graphic.hud.font;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Glyph extends Model {

    private final float width;
    private final float height;
    private final float offsetY;

    public Glyph(float textureX, float textureY, float textureWidth, float textureHeight, float width, float height, float offsetY) {
        this.width = width;
        this.height = height;
        this.offsetY = offsetY;
        this.getMaterial().setDiffuse(new Vector4f(1, 1, 1, 1));
        this.getMaterial().setTextureModulator(1.0f);
        this.setMesh(new GlyphMesh(textureX, textureY, textureWidth, textureHeight, width, height));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getOffsetY() {
        return offsetY;
    }

}
