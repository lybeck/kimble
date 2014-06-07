package kimble.graphic;

import org.lwjgl.LWJGLUtil;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractGraphic {

    private boolean running;

    public AbstractGraphic() {
        setupLWJGL();
    }

    private void setupLWJGL() {
        Screen.setupNativesLWJGL();

        if (!LWJGLUtil.isMacOSXEqualsOrBetterThan(3, 2)) {
            System.err.println("You need an OpenGL version of 3.2 or higher to run this application with a GUI. You can still run the logic just specify the USE_GUI = false");
            System.exit(1);
        }

        Screen.setupDisplay("Kimble - alpha 1.0", 800, 600);

        Screen.setupOpenGL();
        Screen.setResizable(true);
    }

    public void setup() {

    }

    public final void start() {
        if (running) {
            return;
        }
        setup();
        running = true;
        loop();
    }

    public final void stop() {
        running = false;
    }

    private void loop() {
        while (running) {
            Screen.clear();

            float dt = 0.016f;

            update(dt);
            render();

            Screen.update(60);
        }

        dispose();
    }

    public void update(float dt) {

    }

    public void render() {

    }

    public void dispose() {

    }
}
