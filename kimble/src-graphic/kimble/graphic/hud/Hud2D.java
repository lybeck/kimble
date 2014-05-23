package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.camera.Camera;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private final Camera camera;
    private BitmapFont font1;

    // Mapping element id to the element
    private Map<Integer, TextElement> textElements;

    public Hud2D() {
        camera = new Camera2D();
        camera.setupProjectionMatrix();

        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();
    }

    public final void setup() {
        try {
            font1 = FontGenerator.create("font1", new Font("Monospaced", Font.PLAIN, 24), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }
        textElements = new HashMap<>();
        textElements.put(0, new TextElement(font1, "Starting Player: ", 15, 10));
    }

    public void update(float dt) {
        camera.update(dt);
        for (int key : textElements.keySet()) {
            textElements.get(key).update(dt);
        }
    }

    public void setStartingPlayer(String player) {
        textElements.get(0).appendText(player);
    }

    public void render(Shader shader) {
        shader.bind();

        for (int key : textElements.keySet()) {
            textElements.get(key).render(shader, camera);
        }

        shader.unbind();
    }

    private class TextElement {

        private BitmapFont font;
        private String text;
        private int x;
        private int y;

        private Rectangle rectangle;

        public TextElement(BitmapFont font, String text, int x, int y) {
            this.font = font;
            this.x = x;
            this.y = y;

            this.setText(text);
        }

        public final void setText(String text) {
            this.text = text;
            int rectangleWidth = font.calculateWidth(text);
            if (rectangle != null) {
                rectangle.dispose();
            }
            rectangle = new Rectangle(x - 5, y, rectangleWidth, font.getVerticalSpacing(), new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
            rectangle.move(0, 0, -0.01f);
        }

        public final void appendText(String text) {
            setText(this.text + " " + text);
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setBitmapFont(BitmapFont font) {
            this.font = font;
            setText(text);
        }

        public void update(float dt) {
            rectangle.update(dt);
        }

        public void render(Shader shader, Camera camera) {
            rectangle.render(shader, camera);
            font.renderString(shader, camera, text, x, y);
        }
    }
}
