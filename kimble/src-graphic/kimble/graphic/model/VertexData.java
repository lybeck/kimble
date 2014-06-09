package kimble.graphic.model;

import java.util.Objects;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class VertexData {

    private Vector3f position;
    private Vector3f normal;
    private Vector4f color;
    private Vector2f texCoords;

    public static final int positionByteCount = 4 * 3;
    public static final int normalByteCount = 4 * 3;
    public static final int colorByteCount = 4 * 4;
    public static final int texCoordsByteCount = 4 * 2;

    public static final int positionByteOffset = 0;
    public static final int normalByteOffset = positionByteOffset + positionByteCount;
    public static final int colorByteOffset = normalByteOffset + normalByteCount;
    public static final int texCoordsByteOffset = colorByteOffset + colorByteCount;

    public static final int elementCount = 3 + 3 + 4 + 2;
    public static final int stride = positionByteCount + normalByteCount + colorByteCount + texCoordsByteCount;

    public VertexData() {
        position = null;
        normal = null;
        color = new Vector4f(1, 1, 1, 1);
        texCoords = null;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setNormal(Vector3f normal) {
        if (this.normal == null) {
            this.normal = normal;
        } else {
            Vector3f.add(this.normal, normal, this.normal).normalise(this.normal);
        }
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
        if (this.texCoords == null) {
            this.texCoords = texCoords;
        } else {
            this.texCoords = new Vector2f(this.texCoords.x + texCoords.x, this.texCoords.y + texCoords.y);
        }
    }

    public float[] getElements() {
        float[] out = new float[VertexData.elementCount];

        int i = 0;
        out[i++] = position.x;
        out[i++] = position.y;
        out[i++] = position.z;

        if (normal != null) {
            out[i++] = normal.x;
            out[i++] = normal.y;
            out[i++] = normal.z;
        } else {
            out[i++] = 0;
            out[i++] = 1;
            out[i++] = 0;
        }

        out[i++] = color.x;
        out[i++] = color.y;
        out[i++] = color.z;
        out[i++] = color.w;

        if (texCoords != null) {
            out[i++] = texCoords.x;
            out[i++] = texCoords.y;
        } else {
            out[i++] = 1;
            out[i++] = 0;
        }

        return out;
    }

    public Vector3f getPosition() {
        return new Vector3f(position.x, position.y, position.z);
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f getTexCoords() {
        return texCoords;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof VertexData)) {
            return false;
        }

        VertexData other = (VertexData) obj;

        boolean samePosition = this.position.x == other.position.x
                && this.position.y == other.position.y
                && this.position.z == other.position.z;
        boolean sameTexCoord = true; //this.texCoords.equals(other.texCoords);
        boolean sameNormal = true; //this.normal.equals(other.normal);

        return samePosition && sameTexCoord && sameNormal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.position);
        hash = 97 * hash + Objects.hashCode(this.normal);
        hash = 97 * hash + Objects.hashCode(this.color);
        hash = 97 * hash + Objects.hashCode(this.texCoords);
        return hash;
    }
}
