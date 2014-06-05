package kimble.graphic.hud;

import kimble.graphic.camera.Camera;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Button extends AbstractHudItem {

    private final BitmapFont font;

    private String text;
    private Rectangle rectangle;

    private Vector3f position;
    private Vector3f textPosition;
    private Vector3f rotation;

    private float paddingX = 15;

    public Button(String text, BitmapFont font) {
        this.font = font;
        this.rotation = new Vector3f();

        setPosition(0, 0);
        setText(text);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        rectangle.update(dt);
    }

    @Override
    public void render(Shader shader, Camera camera) {
        rectangle.render(shader, camera);
        font.renderString(shader, camera, text, textPosition, rotation);
    }

    public final void setText(String text) {
        this.text = text;
        this.width = font.calculateWidth(text) + 2 * paddingX;
        this.height = font.getVerticalSpacing();
        this.rectangle = new Rectangle(position.x, position.y, width, font.getVerticalSpacing(), new Vector4f(0.4f, 0.4f, 0.4f, 0.4f));
    }

    public final void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.position = new Vector3f(x, y, 0);
        this.textPosition = new Vector3f(x + paddingX, y, 0);
        if (rectangle != null) {
            this.rectangle.setPosition(position);
        }
    }

    public void setPaddingX(float padding) {
        this.paddingX = padding;
    }

}
