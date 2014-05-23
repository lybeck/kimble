package kimble.graphic.hud;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.Screen;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
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
            font36 = FontGenerator.create("font", new Font("Courier New", Font.BOLD, 36), Color.WHITE);
            font24 = FontGenerator.create("font24", new Font("Courier New", Font.PLAIN, 24), Color.WHITE);
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }

        rectangle = new Rectangle(20, 20, Screen.getWidth() - 40, Screen.getHeight() - 40, new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
//        rectangle.setScale(1, 1, 0);
    }

    public void update(float dt) {
        camera.update(dt);
        rectangle.update(dt);
//        font.setPosition(new Vector3f(400, 400, 0));
//        font.update(dt);
    }

    public void render(Shader shader) {
        glDisable(GL_CULL_FACE);

        shader.bind();
        
        // TODO: the cull face is inverted on the RectangleMesh and the GlyphMesh

        font36.renderString(shader, camera, "Testing more text than you can handle", 0, 0);
        font24.renderString(shader, camera, "Testing more text than you can handle", 0, font36.getVerticalSpacing());

        shader.unbind();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
}
