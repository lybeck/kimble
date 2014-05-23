package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.Input2D;
import kimble.graphic.Screen;
import kimble.graphic.camera.Camera;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private final Camera camera;

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

    public final void setup() {
        try {
            font36 = FontGenerator.create("font", new Font("Courier New", Font.PLAIN, 36), new Vector4f(1, 1, 1, 1));
            font24 = FontGenerator.create("font24", new Font("Times New Roman", Font.PLAIN, 24), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }

        rectangle = new Rectangle(20, 20, Screen.getWidth() - 40, Screen.getHeight() - 40, new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
        rectangle.move(0, 0, -0.01f);
    }

    public void update(float dt) {
        camera.update(dt);
        rectangle.update(dt);
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();

        glDisable(GL_CULL_FACE);
        rectangle.render(shader, this.camera);
        font24.renderString(shader, this.camera, this.camera.getPosition().toString(), 0, 0);
        font24.renderString(shader, this.camera, "Hello World!", 0, font24.getVerticalSpacing());

        shader.unbind();
    }
}
