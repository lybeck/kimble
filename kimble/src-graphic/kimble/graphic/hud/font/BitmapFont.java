package kimble.graphic.hud.font;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.TextElement.Word;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.graphic.shader.TextMaterial;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class BitmapFont {

    public static final TextMaterial WHITE = new TextMaterial(new Vector4f(1, 1, 1, 1));
    public static final TextMaterial GREY = new TextMaterial(new Vector4f(0.5f, 0.5f, 0.5f, 1));

    public static final List<TextMaterial> TEXT_MATERIALS = new ArrayList<>();

    static {
        for (int i = 0; i < BoardGraphic.TEAM_COLORS.size(); i++) {
            TEXT_MATERIALS.add(new TextMaterial(BoardGraphic.TEAM_COLORS.get(i)));
        }
    }

    private final Map<Character, Glyph> glyphs;
    private final float verticalSpacing;
    private final String textureKey;

    private Glyph previousGlyph;

    public BitmapFont(Map<Character, Glyph> glyphs, float verticalSpacing, String textureKey) {
        this.glyphs = glyphs;
        this.verticalSpacing = verticalSpacing;
        this.textureKey = textureKey;
        this.previousGlyph = null;
    }

    public void renderString(Shader shader, Camera camera, String line, Vector3f position, Vector3f rotation) {
        TextureManager.getTexture(textureKey).bind();

        renderLine(line, position, rotation, shader, camera, WHITE);

        TextureManager.getTexture(textureKey).unbind();
    }

    public void renderWords(Shader shader, Camera camera, List<Word> words, Vector3f position, Vector3f rotation) {
        TextureManager.getTexture(textureKey).bind();

        for (int j = 0; j < words.size(); j++) {
            Word word = words.get(j);
            renderLine(word.getText(), position, rotation, shader, camera, word.getColor());
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    private void renderLine(String line, Vector3f position, Vector3f rotation, Shader shader, Camera camera, TextMaterial color) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (i == 0) {
                renderGlyph(c, position, rotation, new Matrix4f(), shader, camera, color);
            } else {
                renderGlyph(c, new Vector3f(previousGlyph.getWidth(), 0, 0), new Vector3f(), previousGlyph.getModelMatrix(), shader, camera, color);
            }
        }
    }

    private void renderGlyph(char c, Vector3f position, Vector3f rotation, Matrix4f matrix, Shader shader, Camera camera, TextMaterial color) {
        Glyph g = glyphs.get(c);

        g.setParentModelMatrix(matrix);
        g.setPosition(position);
        g.rotate(rotation.x, rotation.y, rotation.z);

        g.update(0);
        g.render(shader, camera, color);

        previousGlyph = g;
    }

    public Map<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public float calculateWidth(String line) {
        float totalWidth = 0;

        for (int i = 0; i < line.length(); i++) {
            totalWidth += glyphs.get(line.charAt(i)).getWidth();
        }
//        totalWidth += glyphs.get(' ').getWidth();

        return Math.round(totalWidth);
    }

    public float calculateWidth(List<Word> words) {
        float totalWidth = 0;

        for (Word word : words) {
            totalWidth += calculateWidth(word.getText());
        }

        return (int) Math.round(totalWidth);
    }

    public float getVerticalSpacing() {
        return verticalSpacing;
    }

    public String getTextureKey() {
        return textureKey;
    }

}
