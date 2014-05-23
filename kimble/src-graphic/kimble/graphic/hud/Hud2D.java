package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.Screen;
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

    private final Camera2D camera;

    private BitmapFont font36;
    private BitmapFont font24;
    private Rectangle rectangle;

    public Hud2D() {
        camera = new Camera2D();
        camera.setupProjectionMatrix();
        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();
    }

    public void setup() {
        try {
            font36 = FontGenerator.create("font", new Font("Courier New", Font.PLAIN, 36), new Vector4f(1, 1, 1, 1));
            font24 = FontGenerator.create("font24", new Font("Courier New", Font.PLAIN, 24), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }

        rectangle = new Rectangle(20, 20, Screen.getWidth() - 40, Screen.getHeight() - 40, new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
        rectangle.move(0, 0, 1);
    }

    public void update(float dt) {
        camera.update(dt);
        rectangle.update(dt);
    }

    public void render(Shader shader) {
        shader.bind();

        rectangle.render(shader, camera);

        font36.renderString(shader, camera, "Testing more text than you can handle", 0, 0);
        font24.renderString(shader, camera, "Testing more text than you can handle", 0, font36.getVerticalSpacing());

        shader.unbind();
    }
}
