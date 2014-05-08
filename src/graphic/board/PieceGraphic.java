/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic.board;

import graphic.Model;
import graphic.Shader;
import graphic.VertexData;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import logic.Piece;
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
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class PieceGraphic extends Model {

    private int vertexArrayObjectID;
    private int vertexBufferID;
    private int indexBufferID;

    private int indexCount;

    private Vector3f color;
    private final float width;
    private final float height;

    private final Piece pieceLogic;
    private final BoardGraphic board;

    public PieceGraphic(BoardGraphic board, Piece pieceLogic, Vector3f position, Vector3f color, float width, float height) {
        super(position, new Vector3f(0, 0, 0));

        this.board = board;
        this.pieceLogic = pieceLogic;

        this.width = width;
        this.height = height;

        this.color = color;

        setup();
    }

    private void setup() {
        VertexData[] vertices = setupVertexData();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * VertexData.stride);
        for (int i = 0; i < vertices.length; i++) {
            vertexBuffer.put(vertices[i].getElements());
        }
        vertexBuffer.flip();

        byte[] indices = {0, 1, 2, 2, 3, 0,
            0, 1, 5, 5, 4, 0,
            1, 2, 6, 6, 5, 1,
            2, 3, 7, 7, 6, 2,
            3, 0, 4, 4, 7, 3,
            4, 5, 6, 6, 7, 5};
        indexCount = indices.length;
        ByteBuffer indexBuffer = BufferUtils.createByteBuffer(indices.length);
        indexBuffer.put(indices);
        indexBuffer.flip();

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

        indexBufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private VertexData[] setupVertexData() {
        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(-width / 2, 0, width / 2));
        v0.setColor(color);
        v0.setTexCoords(new Vector2f(0, 0));

        VertexData v1 = new VertexData();
        v1.setPosition(new Vector3f(-width / 2, 0, -width / 2));
        v1.setColor(color);
        v1.setTexCoords(new Vector2f(0, 1));

        VertexData v2 = new VertexData();
        v2.setPosition(new Vector3f(width / 2, 0, -width / 2));
        v2.setColor(color);
        v2.setTexCoords(new Vector2f(1, 1));

        VertexData v3 = new VertexData();
        v3.setPosition(new Vector3f(width / 2, 0, width / 2));
        v3.setColor(color);
        v3.setTexCoords(new Vector2f(1, 0));

        VertexData v4 = new VertexData();
        v4.setPosition(new Vector3f(-width / 2, height, width / 2));
        v4.setColor(color);
        v4.setTexCoords(new Vector2f(0, 0));

        VertexData v5 = new VertexData();
        v5.setPosition(new Vector3f(-width / 2, height, -width / 2));
        v5.setColor(color);
        v5.setTexCoords(new Vector2f(0, 1));

        VertexData v6 = new VertexData();
        v6.setPosition(new Vector3f(width / 2, height, -width / 2));
        v6.setColor(color);
        v6.setTexCoords(new Vector2f(1, 1));

        VertexData v7 = new VertexData();
        v7.setPosition(new Vector3f(width / 2, height, width / 2));
        v7.setColor(color);
        v7.setTexCoords(new Vector2f(1, 0));

        return new VertexData[]{v0, v1, v2, v3, v4, v5, v6, v7};
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (pieceLogic.getPosition() != null) {
            int squareID = pieceLogic.getPosition().getID();
            setPosition(board.getSquares().get(squareID).getPosition());
        } else {
            setPosition(board.getEmptyHomeSquare(pieceLogic.getId(), pieceLogic.getTeamId()).getPosition());
        }

    }

    @Override
    public void render(Shader shader) {
        super.render(shader);
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

    @Override
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

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
