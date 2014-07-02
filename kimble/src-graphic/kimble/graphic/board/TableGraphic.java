package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;

/**
 *
 * @author Christoffer
 */
public class TableGraphic extends Model {

    // TODO: fix this to point to the table texture
    private final static String TEXTURE_KEY = "table_wood";

    // TODO: fix this to point to the table model
    private final static String MODEL_KEY = "table";

    public TableGraphic() {
        super();
        this.getMaterial().setTextureModulator(1.0f);
        this.setMesh(ModelManager.getModel(MODEL_KEY));
    }

    @Override
    public void render(Shader shader, Camera camera) {
        TextureManager.getTexture(TEXTURE_KEY).bind();
        super.render(shader, camera);
        TextureManager.getTexture(TEXTURE_KEY).unbind();
    }
}
