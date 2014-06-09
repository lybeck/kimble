package kimble.graphic.input;

import kimble.graphic.camera.Camera2D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public class Input2D extends Input {

    private float mouseSpeed;
    private float moveSpeed;

    public Input2D(Camera2D camera) {
        super(camera);
        this.mouseSpeed = 0.5f;
        this.moveSpeed = 1;
    }

    @Override
    public void inputMouse(float dt) {
        if (Mouse.isGrabbed()) {
            getCamera().getRotation().y += mouseSpeed * dt * Mouse.getDX();
            getCamera().getRotation().x -= mouseSpeed * dt * Mouse.getDY();

            getCamera().getRotation().y %= 2 * Math.PI;
            getCamera().getRotation().x %= 2 * Math.PI;
        }
    }

    @Override
    public void inputKeyboard(float dt) {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            getCamera().move(0, 0, -moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            getCamera().move(0, 0, moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            getCamera().move(-moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            getCamera().move(moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            getCamera().move(0, moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            getCamera().move(0, -moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
    }

    @Override
    public Camera2D getCamera() {
        return (Camera2D) super.getCamera();
    }

    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }

    public float getMouseSpeed() {
        return mouseSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

}
