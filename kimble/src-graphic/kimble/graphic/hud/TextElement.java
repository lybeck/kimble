package kimble.graphic.hud;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.shader.Shader;
import kimble.graphic.shader.TextMaterial;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class TextElement extends AbstractHudElement {

    private final BitmapFont font;

    private final List<Word> words;
    private Vector3f position;

    public TextElement(BitmapFont font) {
        this.font = font;
        this.words = new ArrayList<>();
        this.position = new Vector3f();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.position = new Vector3f(x, y, 0);
        super.setWidth(font.calculateWidth(words));
        super.setHeight(font.getVerticalSpacing());
    }

    public void clear() {
        words.clear();
    }

    public void addWord(String word, TextMaterial color) {
        words.add(new Word(word, color));
    }

    public List<Word> getWords() {
        return words;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(Shader shader, Camera camera) {
        font.renderWords(shader, camera, words, position, new Vector3f());
    }

    @Override
    public void dispose() {
    }

    public class Word {

        private String text;
        private TextMaterial color;

        public Word(String text, TextMaterial color) {
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public TextMaterial getColor() {
            return color;
        }

        public void setColor(TextMaterial color) {
            this.color = color;
        }

    }
}
