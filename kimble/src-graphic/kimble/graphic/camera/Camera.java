package kimble.graphic.camera;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public abstract class Camera {

    protected Matrix4f projectionMatrix;
    protected Matrix4f viewMatrix;

    protected FloatBuffer projectionMatrixBuffer;
    protected FloatBuffer viewMatrixBuffer;

    protected Vector3f position;
    private final Vector3f nPosition;
    protected Vector3f rotation;

    public Camera() {
        this(new Vector3f(), new Vector3f());
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.nPosition = new Vector3f();

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        this.projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
        this.viewMatrixBuffer = BufferUtils.createFloatBuffer(16);

        setupProjectionMatrix();
    }

    public abstract void setupProjectionMatrix();

    public void update(float dt) {
        viewMatrix = new Matrix4f();

        Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), viewMatrix, viewMatrix);

        position.negate(nPosition);
        Matrix4f.translate(nPosition, viewMatrix, viewMatrix);

        viewMatrix.store(viewMatrixBuffer);
        viewMatrixBuffer.flip();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public FloatBuffer getProjectionMatrixBuffer() {
        return projectionMatrixBuffer;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public FloatBuffer getViewMatrixBuffer() {
        return viewMatrixBuffer;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

}
