package kimble.graphic.camera;

import kimble.graphic.Screen;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Camera2D extends Camera {

    public Camera2D() {
        super();
    }

    @Override
    public void setupProjectionMatrix() {
        float width = Screen.getWidth();
        float height = width / Screen.getAspectRatio();
        float left = -width / 2;
        float right = width / 2;
        float top = height / 2;
        float bottom = -height / 2;
        setupOrthographicProjectionMatrix(left, right, top, bottom, -1, 1);
        setPosition(new Vector3f(right, top, 0));
    }

    /**
     * For some weird reason the origin has to be in the center. You can translate the camera to get origin at one
     * corner after you've set the matrix.
     *
     * @param left
     * @param right
     * @param top
     * @param bottom
     * @param zNear
     * @param zFar
     */
    public void setupOrthographicProjectionMatrix(float left, float right, float top, float bottom, float zNear, float zFar) {
        // TODO: Figure out why the origin has to be centered.
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
}
