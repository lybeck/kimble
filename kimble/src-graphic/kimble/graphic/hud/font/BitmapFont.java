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
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class BitmapFont {

    public static final TextMaterial WHITE = new TextMaterial(new Vector4f(1, 1, 1, 1));
    public static final List<TextMaterial> TEXT_MATERIALS = new ArrayList<>();

    static {
        for (int i = 0; i < BoardGraphic.TEAM_COLORS.size(); i++) {
            TEXT_MATERIALS.add(new TextMaterial(BoardGraphic.TEAM_COLORS.get(i)));
        }
    }

    private final Map<Character, Glyph> glyphs;
    private final float verticalSpacing;
    private final String textureKey;

    public BitmapFont(Map<Character, Glyph> glyphs, float verticalSpacing, String textureKey) {
        this.glyphs = glyphs;
        this.verticalSpacing = verticalSpacing;
        this.textureKey = textureKey;
    }

    public void renderString(Shader shader, Camera camera, String line, Vector3f position) {
        TextureManager.getTexture(textureKey).bind();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            position.x = renderGlyph(c, position, shader, camera, WHITE);
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    public void renderText(Shader shader, Camera camera, List<String> words, Vector3f position) {
        TextureManager.getTexture(textureKey).bind();

        for (int j = 0; j < words.size(); j++) {
            String word = words.get(j);
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                position.x = renderGlyph(c, position, shader, camera, WHITE);
            }
            // render a space after each character.
            if (j < words.size() - 1) {
                char c = ' ';
                position.x = renderGlyph(c, position, shader, camera, WHITE);
            }
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    public void renderWords(Shader shader, Camera camera, List<Word> words, Vector3f position) {
        TextureManager.getTexture(textureKey).bind();

        for (int j = 0; j < words.size(); j++) {
            Word word = words.get(j);
            for (int i = 0; i < word.getText().length(); i++) {
                char c = word.getText().charAt(i);
                position.x = renderGlyph(c, position, shader, camera, word.getColor());
            }
            // render a space after each character except the last.
            if (j < words.size() - 1) {
                char c = ' ';
                position.x = renderGlyph(c, position, shader, camera, word.getColor());
            }
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    private float renderGlyph(char c, Vector3f position, Shader shader, Camera camera, TextMaterial color) {
        Glyph g = glyphs.get(c);
        g.setPosition(position);
        g.update(0);
        g.render(shader, camera, color);
        return position.x + g.getWidth();
    }

    public Map<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public int calculateWidth(String line) {
        float totalWidth = 0;

        for (int i = 0; i < line.length(); i++) {
            totalWidth += glyphs.get(line.charAt(i)).getWidth();
        }
        totalWidth += glyphs.get(' ').getWidth();

        return (int) Math.round(totalWidth);
    }

//    public int calculateWidth(List<String> words) {
//        float totalWidth = 0;
//
//        for (String word : words) {
//            totalWidth += calculateWidth(word);
//        }
//
//        return (int) Math.round(totalWidth);
//    }
    public int calculateWidth(List<Word> words) {
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
