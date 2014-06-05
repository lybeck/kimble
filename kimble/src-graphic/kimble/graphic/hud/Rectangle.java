package kimble.graphic.hud;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Rectangle extends Model {

    private float x;
    private float y;
    private float width;
    private float height;

    public Rectangle(float x, float y, float width, float height, Vector4f color) {
        this.setMesh(new RectangleMesh(x, y, width, height, color));

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(Vector3f position) {
        super.setPosition(position);
        this.x = position.x;
        this.y = position.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
