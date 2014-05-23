/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic;

import kimble.graphic.camera.Camera2D;
import kimble.graphic.camera.Camera3D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public class Input2D {

    private Camera2D camera;

    private float mouseSpeed;
    private float moveSpeed;

    public Input2D(Camera2D camera) {
        this.camera = camera;
        this.mouseSpeed = 0.5f;
        this.moveSpeed = 1;
    }

    public void update(float dt) {
        inputMouse(dt);
        inputKeyboard(dt);
    }

    public void inputMouse(float dt) {
        if (Mouse.isGrabbed()) {
            camera.getRotation().y += mouseSpeed * dt * (float) Mouse.getDX();
            camera.getRotation().x -= mouseSpeed * dt * (float) Mouse.getDY();

//            camera.getRotation().x = Math.min(camera.getRotation().x, camera.getMaxYaw());
//            camera.getRotation().x = Math.max(camera.getRotation().x, camera.getMinYaw());
            camera.getRotation().y %= 2 * Math.PI;
            camera.getRotation().x %= 2 * Math.PI;
        }
    }

    public void inputKeyboard(float dt) {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.move(0, 0, -moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.move(0, 0, moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.move(-moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.move(moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            camera.move(0, moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            camera.move(0, -moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
    }

    public void setCamera(Camera2D camera) {
        this.camera = camera;
    }

    public Camera2D getCamera() {
        return camera;
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
