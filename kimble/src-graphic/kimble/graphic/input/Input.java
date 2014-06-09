package kimble.graphic.input;

import kimble.graphic.camera.Camera;

/**
 *
 * @author Christoffer
 */
public abstract class Input {

    private Camera camera;

    public Input() {
        this.camera = null;
    }

    public Input(Camera camera) {
        this.camera = camera;
    }

    public void update(float dt) {
        inputMouse(dt);
        inputKeyboard(dt);
    }

    public abstract void inputMouse(float dt);

    public abstract void inputKeyboard(float dt);

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

}
