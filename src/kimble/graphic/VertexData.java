/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class VertexData {

    private Vector4f position;
    private Vector4f color;
    private Vector2f texCoords;

    public static final int positionByteCount = 4 * 4;
    public static final int colorByteCount = 4 * 4;
    public static final int texCoordsByteCount = 4 * 2;

    public static final int positionByteOffset = 0;
    public static final int colorByteOffset = positionByteOffset + positionByteCount;
    public static final int texCoordsByteOffset = colorByteOffset + colorByteCount;

    public static final int elementCount = 4 + 4 + 2;
    public static final int stride = positionByteCount + colorByteCount + texCoordsByteCount;

    public VertexData() {
        position = new Vector4f();
        color = new Vector4f(1, 1, 1, 1);
        texCoords = new Vector2f();
    }

    public void setPosition(Vector3f position) {
        this.position = new Vector4f(position.x, position.y, position.z, 1);
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setColor(Vector3f color) {
        this.color.x = color.x;
        this.color.y = color.y;
        this.color.z = color.z;
    }

    public void setTexCoords(Vector2f texCoords) {
        this.texCoords = texCoords;
    }

    public float[] getElements() {
        float[] out = new float[VertexData.elementCount];

        int i = 0;
        out[i++] = position.x;
        out[i++] = position.y;
        out[i++] = position.z;
        out[i++] = position.w;

        out[i++] = color.x;
        out[i++] = color.y;
        out[i++] = color.z;
        out[i++] = color.w;

        out[i++] = texCoords.x;
        out[i++] = texCoords.y;

        return out;
    }

    public Vector3f getPosition() {
        return new Vector3f(position.x, position.y, position.z);
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f getTexCoords() {
        return texCoords;
    }
}
