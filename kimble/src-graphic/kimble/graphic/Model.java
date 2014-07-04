package kimble.graphic;

import java.nio.FloatBuffer;
import kimble.graphic.camera.Camera;
import kimble.graphic.model.Mesh;
import kimble.graphic.shader.DefaultMaterial;
import kimble.graphic.shader.Material;
import kimble.graphic.shader.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public abstract class Model {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private Matrix4f parentModelMatrix;
    private Matrix4f modelMatrix;
    private FloatBuffer modelMatrixBuffer;

    private Mesh mesh;
    private DefaultMaterial material;

    public Model() {
        this(new Vector3f(), new Vector3f());
    }

    public Model(Vector3f position, Vector3f rotation) {
        this(position, rotation, new Matrix4f());
    }

    public Model(Vector3f position, Vector3f rotation, Matrix4f parentModelMatrix) {
        this.position = position;
        this.rotation = rotation;
        this.scale = new Vector3f(1, 1, 1);

        this.parentModelMatrix = parentModelMatrix;
        this.modelMatrix = new Matrix4f();
        this.modelMatrixBuffer = BufferUtils.createFloatBuffer(16);

        this.material = new DefaultMaterial();
    }

    public void move(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void rotate(float pitch, float yaw, float roll) {
        this.rotation.x = pitch;
        this.rotation.y = yaw;
        this.rotation.z = roll;
    }

    public void update(float dt) {
        modelMatrix = new Matrix4f();

        Matrix4f.mul(modelMatrix, parentModelMatrix, modelMatrix);

        Matrix4f.scale(scale, modelMatrix, modelMatrix);
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), modelMatrix, modelMatrix);

        modelMatrix.store(modelMatrixBuffer);
        modelMatrixBuffer.flip();
    }

    public void render(Shader shader, Camera camera) {
        this.render(shader, camera, material);
    }

    public void render(Shader shader, Camera camera, Material material) {
        shader.render(camera, getModelMatrixBuffer(), material);
        if (mesh != null) {
            mesh.render();
        }
    }

    public void dispose() {
        if (mesh != null) {
            mesh.dispose();
        }
    }

    public FloatBuffer getModelMatrixBuffer() {
        return modelMatrixBuffer;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void setParentModelMatrix(Matrix4f parentModelMatrix) {
        this.parentModelMatrix = parentModelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMaterial(DefaultMaterial material) {
        this.material = material;
    }

    public DefaultMaterial getMaterial() {
        return material;
    }

    public Vector3f getAabbMax() {
        return getMesh().getAabbMax();
    }

    public Vector3f getAabbMin() {
        return getMesh().getAabbMin();
    }

}
