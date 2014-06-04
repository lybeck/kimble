package kimble.graphic.hud;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.shader.Shader;
import kimble.graphic.shader.TextMaterial;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class TextElement {

    private BitmapFont font;

    private int x;
    private int y;
    private List<Word> words;

    private Rectangle rectangle;

    public TextElement(BitmapFont font) {
        this.font = font;
        this.words = new ArrayList<>();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void clear() {
        words.clear();
    }

    public void addWord(String word, TextMaterial color) {
        words.add(new Word(word, color));

        int rectangleWidth = font.calculateWidth(words);
        if (rectangle != null) {
            rectangle.dispose();
        }
        rectangle = new Rectangle(x - 5, y, rectangleWidth, font.getVerticalSpacing(), new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
        rectangle.move(0, 0, -0.01f);
    }

    public List<Word> getWords() {
        return words;
    }

    public void update(float dt) {
        rectangle.update(dt);
    }

    public void render(Shader shader, Camera camera) {
        rectangle.render(shader, camera);
        font.renderWords(shader, camera, words, x, y);
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

        public TextMaterial getColor() {
            return color;
        }

    }
}
