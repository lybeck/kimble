package kimble.graphic.hud;

import kimble.graphic.Screen;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private final Camera2D camera;

    private BitmapFont font;
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
        font = new BitmapFont(16, 20);
        rectangle = new Rectangle(20, 20, Screen.getWidth() - 40, Screen.getHeight() - 40, new Vector4f(0.2f, 0.2f, 0.2f, 0.4f));
        rectangle.setScale(1, 1, 0);
    }

    public void update(float dt) {
        camera.update(dt);
        rectangle.update(dt);
//        font.setPosition(new Vector3f(400, 400, 0));
//        font.update(dt);
    }

    public void render(Shader shader) {
        shader.bind();
//        rectangle.render(shader, camera);
//        font.render(shader, camera);
        font.renderString(shader, camera, "Hello World!", 10, 10);
        font.renderString(shader, camera, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 10, 30);
        font.renderString(shader, camera, "abcdefghijklmnopqrstuvwxyz", 10, 50);
        shader.unbind();
    }
}
