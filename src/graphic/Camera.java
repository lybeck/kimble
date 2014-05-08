/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Camera {

    private float mouseSpeed;
    private float maxYaw;
    private float minYaw;

    private float moveSpeed;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private static FloatBuffer projectionMatrixBuffer;
    private static FloatBuffer viewMatrixBuffer;

    private Vector3f position;
    private final Vector3f nPosition;
    private Vector3f rotation;

    private float fov;
    private float zNear;
    private float zFar;

    public Camera(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar) {
        this.mouseSpeed = 0.05f;
        this.maxYaw = (float) Math.PI / 2;
        this.minYaw = -(float) Math.PI / 2;

        this.moveSpeed = 2;

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
        viewMatrixBuffer = BufferUtils.createFloatBuffer(16);

        this.position = position;
        this.nPosition = new Vector3f();
        this.rotation = rotation;

        updateProjectionMatrixAttributes(fov, zNear, zFar);
    }

    public void updateProjectionMatrixAttributes() {
        updateProjectionMatrixAttributes(fov, zNear, zFar);
    }

    public void updateProjectionMatrixAttributes(float fov) {
        updateProjectionMatrixAttributes(fov, zNear, zFar);
    }

    public void updateProjectionMatrixAttributes(float fov, float zNear, float zFar) {
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;

        setupPerspectiveProjectionMatrix();
    }

    private void setupPerspectiveProjectionMatrix() {
        float yScale = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
        float xScale = yScale / Screen.getAspectRatio();
        float frustumLength = zFar - zNear;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((zFar + zNear) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * zNear * zFar) / frustumLength);
        projectionMatrix.m33 = 0;

        projectionMatrix.store(projectionMatrixBuffer);
        projectionMatrixBuffer.flip();
    }

    private void input(float dt) {
        inputMouse(dt);
        inputKeyboard(dt);
    }

    private void inputMouse(float dt) {
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        } else if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }

        if (Mouse.isGrabbed()) {
            rotation.y += mouseSpeed * dt * (float) Mouse.getDX();
            rotation.x -= mouseSpeed * dt * (float) Mouse.getDY();

            rotation.x = Math.min(rotation.x, maxYaw);
            rotation.x = Math.max(rotation.x, minYaw);

            rotation.y %= 2 * Math.PI;
            rotation.x %= 2 * Math.PI;
        }
    }

    private void inputKeyboard(float dt) {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            move(0, 0, -moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            move(0, 0, moveSpeed * dt);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            move(-moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            move(moveSpeed * dt, 0, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            move(0, moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            move(0, -moveSpeed * dt, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Mouse.setGrabbed(false);
        }
    }

    public void move(float dx, float dy, float dz) {
        position.x -= dx * (float) Math.sin(rotation.y - Math.PI / 2) + dz * Math.sin(rotation.y);
        position.y += dy;
        position.z += dx * (float) Math.cos(rotation.y - Math.PI / 2) + dz * Math.cos(rotation.y);
    }

    public void update(float dt) {
        input(dt);

        viewMatrix = new Matrix4f();

        Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), viewMatrix, viewMatrix);

        position.negate(nPosition);
        Matrix4f.translate(nPosition, viewMatrix, viewMatrix);

        viewMatrix.store(viewMatrixBuffer);
        viewMatrixBuffer.flip();
    }

    public void setMouseSpeed(float mouseSpeed) {
        this.mouseSpeed = mouseSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setMaxAndMinYaw(float maxYaw, float minYaw) {
        this.maxYaw = maxYaw;
        this.minYaw = minYaw;
    }

    public float getMaxYaw() {
        return maxYaw;
    }

    public float getMinYaw() {
        return minYaw;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static FloatBuffer getProjectionMatrixBuffer() {
        return projectionMatrixBuffer;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public static FloatBuffer getViewMatrixBuffer() {
        return viewMatrixBuffer;
    }

    @Override
    public String toString() {
        return "x = " + position.x + ", y = " + position.y + ", z = " + position.z + "\n"
                + "\t" + "pitch = " + rotation.x + ", yaw = " + rotation.y;
    }

}
