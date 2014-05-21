package kimble.graphic.hud.font;

import java.util.HashMap;
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

    private final int gridSize;
    private final float fontSize;
    private float cellSize;

    private Map<Character, Glyph> glyphs;

    public BitmapFont(int gridSize, float fontSize) {
        this.gridSize = gridSize;
        this.fontSize = fontSize;

        setup();
    }

    private void setup() {
        TextureManager.load("font", BitmapFont.class.getResourceAsStream("/res/fonts/font.png"));
        cellSize = 1.0f / gridSize;

        glyphs = new HashMap<>();
        for (int i = 0; i < gridSize * gridSize; i++) {

            float tx = i % gridSize * cellSize;
            float ty = i / gridSize * cellSize;

            float factor = 1.0f;

            if (i >= 97 && i <= 97 + 25) {
                factor = 0.6f;
                if (i == 'f'
                        || i == 'i'
                        || i == 'j'
                        || i == 'l'
                        || i == 't'
                        || i == 'r') {
                    factor = 0.4f;
                } else if (i == 'm'
                        || i == 'w') {
                    factor = 0.9f;
                }
            } else if (i >= 65 && i <= 65 + 25) {
                factor = 0.8f;
                if (i == 'G'
                        || i == 'H') {
                    System.out.println("G or H");
                }
            }
            Glyph g = new Glyph(tx, ty, cellSize * factor, cellSize, fontSize * factor, fontSize);
            glyphs.put((char) i, g);
        }
    }

    public void renderString(Shader shader, Camera camera, String line, float x, float y) {
        TextureManager.getTexture("font").bind();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            glyphs.get(c).setPosition(new Vector3f(x, y, 0));
            glyphs.get(c).update(0);
            glyphs.get(c).render(shader, camera);
            x += glyphs.get(c).getWidth();
        }
        TextureManager.getTexture("font").unbind();
    }

}
