/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieHolderGraphic extends Model {
    
    public DieHolderGraphic() {
        this.getMaterial().setDiffuse(new Vector4f(0.2f, 0, 0, 1f));
        this.setMesh(ModelManager.getModel("game_board_die_holder"));
    }
    
}
