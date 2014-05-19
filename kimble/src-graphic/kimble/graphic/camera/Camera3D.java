/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.camera;

import kimble.graphic.Screen;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Camera3D extends Camera {

    private float maxYaw;
    private float minYaw;

    private float fov;
    private float zNear;
    private float zFar;

    public Camera3D(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar) {
        super();

        this.maxYaw = (float) Math.PI / 2;
        this.minYaw = -(float) Math.PI / 2;
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public final void updateProjectionMatrixAttributes(float fov, float zNear, float zFar) {
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    @Override
    public void setupProjectionMatrix() {
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

    public void setupOrthographicProjectionMatrix() {
        float width = Screen.getWidth();
        float height = width / Screen.getAspectRatio();
        float left = -width / 2;
        float right = width / 2;
        float top = height / 2;
        float bottom = -height / 2;
        setupOrthographicProjectionMatrix(left, right, top, bottom, -1, 1);
    }

    public void setupOrthographicProjectionMatrix(float left, float right, float top, float bottom, float zNear, float zFar) {
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = 2 / (right - left);
        projectionMatrix.m11 = 2 / (top - bottom);
        projectionMatrix.m22 = -2 / (zFar - zNear);
        projectionMatrix.m03 = -(right + left) / (right - left);
        projectionMatrix.m13 = -(top + bottom) / (top - bottom);
        projectionMatrix.m23 = -(zFar + zNear) / (zFar - zNear);
        projectionMatrix.m33 = 1;

        projectionMatrix.store(projectionMatrixBuffer);
        projectionMatrixBuffer.flip();
    }

    public void move(float dx, float dy, float dz) {
        position.x -= dx * (float) Math.sin(rotation.y - Math.PI / 2) + dz * Math.sin(rotation.y);
        position.y += dy;
        position.z += dx * (float) Math.cos(rotation.y - Math.PI / 2) + dz * Math.cos(rotation.y);
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

}
