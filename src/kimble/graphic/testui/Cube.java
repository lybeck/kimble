/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.testui;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Cube extends Model {

    public Cube() {
        super(new Vector3f(0, 0.7f, 0), new Vector3f(0, 0, 0)); //(float) Math.toRadians(90)));

        this.getMaterial().setTextureModulator(0.95f);
        this.setMesh(ModelManager.getModel("cube"));
    }

    float angle = 0;

    @Override
    public void update(float dt) {
        angle += dt;
        super.update(dt);
        rotate(angle, angle, angle);
    }

    @Override
    public void render(Shader shader) {
        TextureManager.getTexture("temp_tex").bind();
        super.render(shader);
        TextureManager.getTexture("temp_tex").unbind();
    }
}
