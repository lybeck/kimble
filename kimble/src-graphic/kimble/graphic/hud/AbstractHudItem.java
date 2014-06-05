package kimble.graphic.hud;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.Screen;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractHudItem {

    private final List<Callback> callbacks;

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public AbstractHudItem() {
        this.callbacks = new ArrayList<>();
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
                if (Mouse.isButtonDown(0)) {
                    execute();
                }
            }
        }

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

    public List<Callback> getCallbacks() {
        return callbacks;
    }

}
