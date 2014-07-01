package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Cube extends Model {

    float angle = 0;

    public Cube() {
        super(new Vector3f(0, 0.7f, 0), new Vector3f(0, 0, 0)); //(float) Math.toRadians(90)));

        this.getMaterial().setTextureModulator(0.95f);
        this.setMesh(ModelManager.getModel("cube"));
    }

    @Override
    public void update(float dt) {
        angle += dt;
        super.update(dt);
        rotate(angle, angle, angle);
    }

    @Override
    public void render(Shader shader, Camera camera) {
        TextureManager.getTexture("temp_tex").bind();
        super.render(shader, camera);
        TextureManager.getTexture("temp_tex").unbind();
    }
}
