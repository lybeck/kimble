/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.graphic.testui;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;

/**
 *
 * @author Christoffer
 */
public class Cube extends Model {
    
    public Cube() {
        super();
        
        this.setMesh(ModelManager.getModel("cube"));
    }
}
