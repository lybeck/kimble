/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.loading;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import kimble.graphic.VertexData;
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
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class OBJModel {

    private int vertexArrayObjectID;
    private int vertexBufferID;
    private int indexBufferID;

    private int indexCount;

    private List<OBJLoader.Face> faceList;
    private List<Vector3f> vertexList;
    private List<Vector3f> normalList;

    public OBJModel(Vector3f color, List<OBJLoader.Face> faces, List<Vector3f> vertices, List<Vector3f> normals) {
        this.faceList = faces;
        this.vertexList = vertices;
        this.normalList = normals;

        setup();
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
        int[] indices = setupIndexData();
        indexCount = indices.length;
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

        indexBufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private VertexData[] setupVertexData() {
        VertexData[] vertices = new VertexData[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            vertices[i] = new VertexData();
            vertices[i].setPosition(vertexList.get(i));
        }
        return vertices;
    }

    private int[] setupIndexData() {
        int[] indices = new int[faceList.size() * 3];

        int index = 0;
        for (int i = 0; i < faceList.size(); i++) {
            OBJLoader.Face face = faceList.get(i);
            indices[index++] = face.getVertexIndices()[0];
            indices[index++] = face.getVertexIndices()[1];
            indices[index++] = face.getVertexIndices()[2];
        }

        return indices;
    }

    public void render() {
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
}
