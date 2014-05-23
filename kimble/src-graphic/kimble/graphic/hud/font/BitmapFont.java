package kimble.graphic.hud.font;

import java.util.Map;
import kimble.graphic.camera.Camera;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BitmapFont {

    private final Map<Character, Glyph> glyphs;
    private final float verticalSpacing;
    private final String textureKey;

    public BitmapFont(Map<Character, Glyph> glyphs, float verticalSpacing, String textureKey) {
        this.glyphs = glyphs;
        this.verticalSpacing = verticalSpacing;
        this.textureKey = textureKey;
    }

    public void renderString(Shader shader, Camera camera, String line, float x, float y) {
        TextureManager.getTexture(textureKey).bind();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            Glyph g = glyphs.get(c);
            g.setPosition(new Vector3f(x, y + g.getOffsetY() + (int) verticalSpacing, 0));
            g.update(0);
            g.render(shader, camera);
            x += g.getWidth();
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    public Map<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public float getVerticalSpacing() {
        return verticalSpacing;
    }

    public String getTextureKey() {
        return textureKey;
    }

}
