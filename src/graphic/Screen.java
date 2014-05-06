/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Christoffer
 */
public class Screen {

    public static void setupDisplay(String title, int width, int height) {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(title);
            Display.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.2f, 0.2f, 0f, 1f);
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
        return (float) Display.getWidth() / Display.getHeight();
    }

    public static void cleanUp() {
        Display.destroy();
    }

    public static void setupLWJGL() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_TEXTURE_RECTANGLE_ARB);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void updateViewport(Camera camera, boolean windowResized) {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        camera.setAspectRatio(getAspectRatio());
        if (windowResized) {
            camera.applyPerspectiveMatrix();
        }
    }

    /**
     * Loads the natives for LWJGL, from the natives folder inside the lib
     * directory. By using this method you don't have to explicitly have to
     * choose natives (because they are different for different platforms).
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
