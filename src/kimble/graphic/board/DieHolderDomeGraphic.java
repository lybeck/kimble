/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieHolderDomeGraphic extends Model {

    public DieHolderDomeGraphic() {
        this.getMaterial().setDiffuse(new Vector4f(0.7f, 0.7f, 1, 0.2f));
        this.getMaterial().setSpecular(new Vector4f(1, 1, 1, 0.2f));
        this.getMaterial().setAmbient(new Vector4f(0.2f, 0.2f, 0.2f, 0.2f));
        this.setPosition(new Vector3f(0, 0.4f, 0));
        this.setMesh(ModelManager.getModel("game_board_die_holder_dome"));
    }
    
}
