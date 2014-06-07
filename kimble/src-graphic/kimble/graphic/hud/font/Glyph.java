package kimble.graphic.hud.font;

import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.shader.DefaultMaterial;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Glyph extends Model {

    private final float width;
    private final float height;

    public Glyph(float textureX, float textureY, float textureWidth, float textureHeight, float width, float height, Vector4f color) {
        this.width = width;
        this.height = height;
        this.getMaterial().setDiffuse(new Vector4f(1, 1, 1, 1));
        this.setMesh(new GlyphMesh(textureX, textureY, textureWidth, textureHeight, width, height, color));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public void render(Shader shader, Camera camera, DefaultMaterial material){
        super.render(shader, camera, material);
    }

}
