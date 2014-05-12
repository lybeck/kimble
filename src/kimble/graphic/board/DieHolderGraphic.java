/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;

/**
 *
 * @author Christoffer
 */
public class DieHolderGraphic extends Model {
    
    public DieHolderGraphic() {
        super();
        
        this.setMesh(ModelManager.getModel("game_board_die_holder"));
    }
    
}
