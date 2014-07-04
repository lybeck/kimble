package kimble.graphic.hud;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.graphic.camera.Camera;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.shader.Shader;
import kimble.logic.Team;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractHud {

    private final AbstractKimbleGraphic graphic;
    private final Camera camera;

    private final List<Team> teams;

    private List<AbstractHudElement> elements;

    public AbstractHud(AbstractKimbleGraphic graphic, List<Team> teams) {
        this(graphic, teams, new Camera2D());
    }

    public AbstractHud(AbstractKimbleGraphic graphic, List<Team> teams, Camera camera) {
        this.graphic = graphic;
        this.teams = teams;
        this.camera = camera;
        this.camera.setupProjectionMatrix();

        this.elements = new ArrayList<>();
        setup();
    }

    public void setup() {
        updateViewport();
    }

    public void addElement(AbstractHudElement element) {
        elements.add(element);
    }

    public void addElements(List<AbstractHudElement> elements) {
        for (AbstractHudElement element : elements) {
            this.elements.add(element);
        }
    }

    // =======================================================
    // Update and render
    // =======================================================
    public void updateViewport() {
        camera.setupProjectionMatrix();
    }

    public void update(float dt) {
        camera.update(dt);

        for (AbstractHudElement element : elements) {
            element.update(dt);
        }
    }

    /**
     * The shader needs to be bound before calling this method
     *
     * @param shader
     */
    public final void render(Shader shader) {
        glDisable(GL_DEPTH_TEST);

        preRender(shader, camera);

        for (AbstractHudElement element : elements) {
            element.render(shader, camera);
        }

        postRender(shader, camera);

        glEnable(GL_DEPTH_TEST);
    }

    public void preRender(Shader shader, Camera camera) {

    }

    public void postRender(Shader shader, Camera camera) {

    }

    public void dispose() {
        for (AbstractHudElement element : elements) {
            element.dispose();
        }
    }

    public List<Team> getTeams() {
        return teams;
    }

    public AbstractKimbleGraphic getGraphic() {
        return graphic;
    }
}
