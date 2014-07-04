package kimble.graphic.hud;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.Screen;
import kimble.graphic.camera.Camera;
import kimble.graphic.shader.Shader;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractHudElement {

    private final List<Callback> callbacks;

    private float x;
    private float y;
    private float width;
    private float height;

    private boolean enabled;

    public AbstractHudElement() {
        this.callbacks = new ArrayList<>();
        this.enabled = true;
    }

    public void addCallback(Callback callback) {
        callbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        callbacks.remove(callback);
    }

    public void execute() {
        for (Callback callback : callbacks) {
            callback.execute();
        }
    }

    public void update(float dt) {

        int mouseX = Mouse.getX();
        int mouseY = Screen.getHeight() - Mouse.getY();

        if (mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height) {

            while (Mouse.next()) {
                if (Mouse.getEventButtonState()) {
                    if (Mouse.getEventButton() == 0) {
                        execute();
                    }
                }
            }
        }
    }

    public abstract void render(Shader shader, Camera camera);

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
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

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Callback> getCallbacks() {
        return callbacks;
    }

    public abstract void dispose();

}
