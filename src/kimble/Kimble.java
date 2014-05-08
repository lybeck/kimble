package kimble;

import kimble.graphic.Screen;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    public static void main(String[] args) {
        setupLWJGL();
        new GameTest();
    }

    private static void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }
}
