/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.graphic.shader.Material;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieHolderDomeGraphic extends Model {

    private Material material;

    public DieHolderDomeGraphic() {
        super();

        this.material = new Material();
        this.material.setDiffuse(new Vector4f(0.7f, 0.7f, 1, 0.2f));
        this.material.setSpecular(new Vector4f(1, 1, 1, 0.2f));
        this.material.setAmbient(new Vector4f(0.2f, 0.2f, 0.2f, 0.2f));
        this.setMesh(ModelManager.getModel("game_board_die_holder_dome"));
    }

    @Override
    public void render(Shader shader) {
        super.render(shader, material);
    }
}
