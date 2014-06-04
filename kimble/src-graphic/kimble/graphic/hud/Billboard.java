/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.hud;

import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.shader.Shader;

/**
 *
 * @author Christoffer
 */
public class Billboard extends Model {

    private final String text;
    private final BitmapFont font;

    public Billboard(String text, BitmapFont font) {
        this.text = text;
        this.font = font;

        float width = font.calculateWidth(text);
        float height = font.getVerticalSpacing();
    }
    
    @Override
    public void render(Shader shader, Camera camera){
//        font.ren
    }

}
