/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.hud;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Rectangle extends Model {

    public Rectangle(float x, float y, float width, float height, Vector4f color) {
        this.getMaterial().setDiffuse(color);
        this.setMesh(new RectangleMesh(x, y, width, height, color));
    }
}
