/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import kimble.graphic.Screen;
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

    protected boolean useHud;

    public AbstractGraphic(boolean useHud) {
        this.useHud = useHud;
        setupLWJGL();
    }

    private void setupLWJGL() {
        Screen.setupNativesLWJGL();

        if (!LWJGLUtil.isMacOSXEqualsOrBetterThan(3, 2)) {
            System.err.println("You need an OpenGL version of 3.2 or higher to run this application!");
        }

        System.out.println("*************************************************");

        if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
            System.out.println("Using MacOSx - no support for the HUD.");
            Screen.setupDisplayMacOsx("Kimble - beta 0.1", 800, 600);
            useHud = false;
        } else {
            Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        }

        System.out.println("OS name " + System.getProperty("os.name"));
        System.out.println("OS version " + System.getProperty("os.version"));
        System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
        System.out.println("OpenGL version " + glGetString(GL_VERSION));
        System.out.println("OpenGL shading language " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        System.out.println("*************************************************");

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
