/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.shader.Shader;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.logic.Die;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieGraphic extends Model {

    private final Die die;
    private float angle = 0;

    public DieGraphic(Die die) {

        this.die = die;

        this.getMaterial().setDiffuse(new Vector4f(0, 0, 0, 1));
        this.getMaterial().setTextureModulator(1.0f);
        this.setPosition(new Vector3f(0, 0.7f, 0));
        this.setMesh(ModelManager.getModel("cube"));
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        angle += dt;
        rotate(angle, angle, angle);
    }

    @Override
    public void render(Shader shader) {
        TextureManager.getTexture("temp_tex").bind();
        super.render(shader);
        TextureManager.getTexture("temp_tex").unbind();
    }

}
