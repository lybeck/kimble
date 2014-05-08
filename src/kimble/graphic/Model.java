/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public abstract class Model {

    private int vertexArrayObjectID;
    private int vertexBufferID;
    private int indexBufferID;

    private int indexCount;

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

    public final void setup() {
        setupVertexBuffer();
        setupIndexBuffer();
    }

    private void setupVertexBuffer() {
        VertexData[] vertices = setupVertexData();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * VertexData.stride);
        for (int i = 0; i < vertices.length; i++) {
            vertexBuffer.put(vertices[i].getElements());
        }
        vertexBuffer.flip();

        vertexArrayObjectID = glGenVertexArrays();
        glBindVertexArray(vertexArrayObjectID);
        {
            vertexBufferID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, VertexData.stride, VertexData.positionByteOffset);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, VertexData.stride, VertexData.colorByteOffset);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, VertexData.stride, VertexData.texCoordsByteOffset);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        glBindVertexArray(0);
    }

    private void setupIndexBuffer() {
        byte[] indices = setupIndexData();
        indexCount = indices.length;
        ByteBuffer indexBuffer = BufferUtils.createByteBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

        indexBufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public abstract VertexData[] setupVertexData();

    public abstract byte[] setupIndexData();

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
        glBindVertexArray(vertexArrayObjectID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_BYTE, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);
    }

    public void cleanUp() {
        glBindVertexArray(vertexArrayObjectID);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vertexBufferID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(indexBufferID);

        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayObjectID);
    }

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
