/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import graphic.Shader;
import java.nio.FloatBuffer;
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

    private Matrix4f modelMatrix;
    private FloatBuffer modelMatrixBuffer;

    public Model() {
        this(new Vector3f(), new Vector3f());
    }

    public Model(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.scale = new Vector3f(1, 1, 1);

        this.modelMatrix = new Matrix4f();
        this.modelMatrixBuffer = BufferUtils.createFloatBuffer(16);
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

        Matrix4f.scale(scale, modelMatrix, modelMatrix);
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.z, new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.y, new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
        Matrix4f.rotate(rotation.x, new Vector3f(1, 0, 0), modelMatrix, modelMatrix);

        modelMatrix.store(modelMatrixBuffer);
        modelMatrixBuffer.flip();
    }

    public void render(Shader shader) {
        shader.render(getModelMatrixBuffer());
    }

    public abstract void cleanUp();

    public FloatBuffer getModelMatrixBuffer() {
        return modelMatrixBuffer;
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
}
