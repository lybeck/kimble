package kimble;

import kimble.graphic.Screen;

/**
 *
 * @author Lasse Lybeck
 */
public class Kimble {

    public static void main(String[] args) {
        boolean noGui = false;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("nogui")) {
                noGui = true;
            }
        }
        if (!noGui) {
            setupLWJGL();
        }
        new TestGame(noGui);
    }

    private static void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }
}
