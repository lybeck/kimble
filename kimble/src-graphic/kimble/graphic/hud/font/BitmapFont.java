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
    private final int verticalSpacing;
    private final String textureKey;

    public BitmapFont(Map<Character, Glyph> glyphs, int verticalSpacing, String textureKey) {
        this.glyphs = glyphs;
        this.verticalSpacing = verticalSpacing;
        this.textureKey = textureKey;
    }

    public void renderString(Shader shader, Camera camera, String line, int x, int y) {
        TextureManager.getTexture(textureKey).bind();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            x = renderGlyph(c, x, y, shader, camera, WHITE);
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    public void renderText(Shader shader, Camera camera, List<String> words, int x, int y) {
        TextureManager.getTexture(textureKey).bind();

        for (int j = 0; j < words.size(); j++) {
            String word = words.get(j);
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                x = renderGlyph(c, x, y, shader, camera, WHITE);
            }
            // render a space after each character.
            if (j < words.size() - 1) {
                char c = ' ';
                x = renderGlyph(c, x, y, shader, camera, WHITE);
            }
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    public void renderWords(Shader shader, Camera camera, List<Word> words, int x, int y) {
        TextureManager.getTexture(textureKey).bind();

        for (int j = 0; j < words.size(); j++) {
            Word word = words.get(j);
            for (int i = 0; i < word.getText().length(); i++) {
                char c = word.getText().charAt(i);
                x = renderGlyph(c, x, y, shader, camera, word.getColor());
            }
            // render a space after each character.
            if (j < words.size() - 1) {
                char c = ' ';
                x = renderGlyph(c, x, y, shader, camera, word.getColor());
            }
        }

        TextureManager.getTexture(textureKey).unbind();
    }

    private int renderGlyph(char c, int x, int y, Shader shader, Camera camera, TextMaterial color) {
        Glyph g = glyphs.get(c);
        g.setPosition(new Vector3f(x, y, 0));
        g.update(0);
        g.render(shader, camera, color);
        x += g.getWidth();
        return x;
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

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public String getTextureKey() {
        return textureKey;
    }

}
