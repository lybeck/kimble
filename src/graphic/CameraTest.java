/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import graphic.Screen;
import logic.Game;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class CameraTest {

    private Camera camera;
    private Shader shader;
    private BoardGraphic boardGraphic;

    public CameraTest() {
        setup();
        run();
        cleanUp();
    }

    private void setup() {

        Screen.setupNativesLWJGL();
        Screen.setupDisplay("OpenGL 3.2 Camera Test", 800, 600);
        Screen.setupOpenGL();

        camera = new Camera(new Vector3f(0, 50, 10), new Vector3f(45, 0, 0), 70f, 0.3f, 1000f);

        shader = new Shader("res/shaders/shader.vert", "res/shaders/shader.frag");

        Game game = new Game();
        boardGraphic = new BoardGraphic(game.getBoard(), game.getNumberOfTeams(), 5, 2);
    }

    private void run() {

        while (!Screen.isCloseRequested()) {
            Screen.clear();

            update(0.16f);
            render();

            Screen.update(60);
        }

    }

    private void update(float dt) {
        camera.update(dt);
        boardGraphic.update(dt);
    }

    private void render() {
        shader.bind();
        boardGraphic.render(shader);
        shader.unbind();
    }

    private void cleanUp() {
        shader.cleanUp();
        boardGraphic.cleanUp();
        Screen.cleanUp();
    }

    public static void main(String[] args) {
        new CameraTest();
    }
}
