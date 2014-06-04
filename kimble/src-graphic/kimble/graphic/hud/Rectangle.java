package kimble.graphic.hud;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Rectangle extends Model {

    public Rectangle(float x, float y, float width, float height, Vector4f color) {
        this.setMesh(new RectangleMesh(x, y, width, height, color));
    }
}
