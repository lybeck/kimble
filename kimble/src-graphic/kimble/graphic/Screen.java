package kimble.graphic;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author Christoffer
 */
public class Screen {

    public static void setupDisplay(String title, int width, int height) {
        try {
            // TODO: Check what the arguments are!
            PixelFormat pixelFormat = new PixelFormat(8, 1, 0, 8);
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                    .withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(title);
            Display.create(pixelFormat, contextAtrributes);
        } catch (LWJGLException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void setVSyncEnabled(boolean vSync) {
        Display.setVSyncEnabled(vSync);
    }

    public static void setResizable(boolean resiazable) {
        Display.setResizable(resiazable);
    }

    public static boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public static boolean wasResized() {
        return Display.wasResized();
    }

    /**
     * If fps <= 0 will the Display.sync(fps) method call be omitted.
     *
     * @param fps
     */
    public static void update(int fps) {
        if (fps > 0) {
            Display.sync(fps);
        }
        Display.update();
    }

    public static float getAspectRatio() {
        return (float) Display.getWidth() / (float) Display.getHeight();
    }

    public static void dispose() {
        Display.destroy();
    }

    public static void setupOpenGL() {
        glClearColor(0.4f, 0.6f, 0.9f, 0f);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static void updateViewport() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static int getWidth() {
        return Display.getWidth();
    }

    public static int getHeight() {
        return Display.getHeight();
    }

    /**
     * Loads the natives for LWJGL, from the natives folder inside the lib directory. By using this method you don't
     * have to explicitly have to choose natives (because they are different for different platforms).
     */
    public static void setupNativesLWJGL() {
        String lwjglPath = "org.lwjgl.librarypath";
        String userDir = System.getProperty("user.dir");

        String nativePath = "lib/natives";

        File nativeFile = new File(new File(userDir, nativePath),
                LWJGLUtil.getPlatformName());

        System.setProperty(lwjglPath, nativeFile.getAbsolutePath());

        String inputPath = "net.java.games.input.librarypath";
        String lwjglProperty = System.getProperty(lwjglPath);

        System.setProperty(inputPath, lwjglProperty);
    }
}
