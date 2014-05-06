/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import graphic.Screen;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Camera {

    private Vector3f position;
    private Vector3f rotation;
    private float fov;
    private float aspectRatio;
    private final float zNear;
    private final float zFar;

    public Camera(float fov, Vector3f position, Vector3f rotation, float zNear, float zFar) {
        if (zNear <= 0) {
            throw new IllegalArgumentException("zNear " + zNear + " was 0 or was smaller than 0");
        }
        if (zFar <= zNear) {
            throw new IllegalArgumentException("zFar " + zFar + " was smaller or the same as zNear " + zNear);
        }
        this.aspectRatio = Screen.getAspectRatio();
        this.fov = fov;
        this.position = position;
        this.rotation = rotation;
        this.zNear = zNear;
        this.zFar = zFar;

        Screen.updateViewport(this, true);
    }

    /**
     * Processes mouse input and converts it in to camera movement.
     *
     * @param mouseSpeed the speed (sensitivity) of the mouse, 1.0 should
     * suffice
     */
    public void processMouse(float mouseSpeed) {
        final float MAX_LOOK_UP = 90;
        final float MAX_LOOK_DOWN = -90;
        float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
        float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
        if (rotation.y + mouseDX >= 360) {
            rotation.y += mouseDX - 360;
        } else if (rotation.y + mouseDX < 0) {
            rotation.y = 360 - rotation.y + mouseDX;
        } else {
            rotation.y += mouseDX;
        }
        if (rotation.x - mouseDY >= MAX_LOOK_DOWN && rotation.x - mouseDY <= MAX_LOOK_UP) {
            rotation.x += -mouseDY;
        } else if (rotation.x - mouseDY < MAX_LOOK_DOWN) {
            rotation.x = MAX_LOOK_DOWN;
        } else if (rotation.x - mouseDY > MAX_LOOK_UP) {
            rotation.x = MAX_LOOK_UP;
        }
    }

    /**
     * Processes keyboard input and converts into camera movement.
     *
     * @param delta the elapsed time since the last frame update in milliseconds
     * @param speed the speed of the movement (normal = 1.0)
     *
     * @throws IllegalArgumentException if delta is 0 or delta is smaller than 0
     */
    public void processKeyboard(float delta, float speed) {
        processKeyboard(delta, speed, speed, speed);
    }

    /**
     * Processes keyboard input and converts into camera movement.
     *
     * @param delta the elapsed time since the last frame update in milliseconds
     * @param speedX the speed of the movement on the x-axis (normal = 1.0)
     * @param speedY the speed of the movement on the y-axis (normal = 1.0)
     * @param speedZ the speed of the movement on the z-axis (normal = 1.0)
     *
     * @throws IllegalArgumentException if delta is 0 or delta is smaller than 0
     */
    public void processKeyboard(float delta, float speedX, float speedY, float speedZ) {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta " + delta + " is 0 or is smaller than 0");
        }

        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (keyUp && keyRight && !keyLeft && !keyDown) {
            moveFromLook(speedX * delta * 0.003f, 0, -speedZ * delta * 0.003f);
        }
        if (keyUp && keyLeft && !keyRight && !keyDown) {
            moveFromLook(-speedX * delta * 0.003f, 0, -speedZ * delta * 0.003f);
        }
        if (keyUp && !keyLeft && !keyRight && !keyDown) {
            moveFromLook(0, 0, -speedZ * delta * 0.003f);
        }
        if (keyDown && keyLeft && !keyRight && !keyUp) {
            moveFromLook(-speedX * delta * 0.003f, 0, speedZ * delta * 0.003f);
        }
        if (keyDown && keyRight && !keyLeft && !keyUp) {
            moveFromLook(speedX * delta * 0.003f, 0, speedZ * delta * 0.003f);
        }
        if (keyDown && !keyUp && !keyLeft && !keyRight) {
            moveFromLook(0, 0, speedZ * delta * 0.003f);
        }
        if (keyLeft && !keyRight && !keyUp && !keyDown) {
            moveFromLook(-speedX * delta * 0.003f, 0, 0);
        }
        if (keyRight && !keyLeft && !keyUp && !keyDown) {
            moveFromLook(speedX * delta * 0.003f, 0, 0);
        }
        if (flyUp && !flyDown) {
            position.y += speedY * delta * 0.003f;
        }
        if (flyDown && !flyUp) {
            position.y -= speedY * delta * 0.003f;
        }
    }

    /**
     * Move in the direction you're looking. That is, this method assumes a new
     * coordinate system where the axis you're looking down is the z-axis, the
     * axis to your left is the x-axis, and the upward axis is the y-axis.
     *
     * @param dx the movement along the x-axis
     * @param dy the movement along the y-axis
     * @param dz the movement along the z-axis
     */
    public void moveFromLook(float dx, float dy, float dz) {
        this.position.z += dx * (float) cos(toRadians(rotation.y - 90)) + dz * cos(toRadians(rotation.y));
        this.position.x -= dx * (float) sin(toRadians(rotation.y - 90)) + dz * sin(toRadians(rotation.y));
        this.position.y += dy * (float) sin(toRadians(rotation.x - 90)) + dz * sin(toRadians(rotation.x));
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Sets GL_PROJECTION to an orthographic projection matrix. The matrix mode
     * will be returned it its previous value after execution.
     */
    public void applyOrthographicMatrix() {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-aspectRatio, aspectRatio, -1, 1, 0, zFar);
        glPopAttrib();
    }

    /**
     * Sets GL_PROJECTION to an perspective projection matrix. The matrix mode
     * will be returned it its previous value after execution.
     */
    public void applyPerspectiveMatrix() {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fov, aspectRatio, zNear, zFar);
        glPopAttrib();
    }

    public void applyTranslations() {
        glPushAttrib(GL_TRANSFORM_BIT);
        glMatrixMode(GL_MODELVIEW);
        glRotatef(rotation.x, 1, 0, 0);
        glRotatef(rotation.y, 0, 1, 0);
        glRotatef(rotation.z, 0, 0, 1);
        glTranslatef(-position.x, -position.y, -position.z);
        glPopAttrib();
    }

    public void setRotation(float pitch, float yaw, float roll) {
        this.rotation = new Vector3f(pitch, yaw, roll);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float fieldOfView() {
        return fov;
    }

    public void setFieldOfView(float fov) {
        this.fov = fov;
    }

    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio <= 0) {
            throw new IllegalArgumentException("aspectRatio " + aspectRatio + " is 0 or less");
        }
        this.aspectRatio = aspectRatio;
    }

    public float aspectRatio() {
        return aspectRatio;
    }

    public float nearClippingPane() {
        return zNear;
    }

    public float farClippingPane() {
        return zFar;
    }

    @Override
    public String toString() {
        return "Camera [x=" + position.x + ", y=" + position.y + ", z=" + position.z + ", pitch=" + rotation.x + ", yaw=" + rotation.y + ", "
                + "roll=" + rotation.z + ", fov=" + fov + ", aspectRatio=" + aspectRatio + ", zNear=" + zNear + ", "
                + "zFar=" + zFar + "]";
    }

}
