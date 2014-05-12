/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import java.util.ArrayList;
import java.util.List;
import kimble.graphic.Model;
import kimble.graphic.shader.Shader;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieGraphic extends Model {
    
    public static final Vector3f ROTATION_ONE = new Vector3f((float) Math.toRadians(90), 0, 0);
    public static final Vector3f ROTATION_TWO = new Vector3f(0, 0, (float) Math.toRadians(-90));
    public static final Vector3f ROTATION_THREE = new Vector3f(0, 0, 0);
    public static final Vector3f ROTATION_FOUR = new Vector3f((float) Math.toRadians(180), 0, 0);
    public static final Vector3f ROTATION_FIVE = new Vector3f(0, 0, (float) Math.toRadians(90));
    public static final Vector3f ROTATION_SIX = new Vector3f((float) Math.toRadians(-90), 0, 0);
    
    private Vector3f rotation;
    
    public static final List<Vector3f> DIE_ROLL_ROTATIONS = new ArrayList<>();

    static {
        DIE_ROLL_ROTATIONS.add(ROTATION_ONE);
        DIE_ROLL_ROTATIONS.add(ROTATION_TWO);
        DIE_ROLL_ROTATIONS.add(ROTATION_THREE);
        DIE_ROLL_ROTATIONS.add(ROTATION_FOUR);
        DIE_ROLL_ROTATIONS.add(ROTATION_FIVE);
        DIE_ROLL_ROTATIONS.add(ROTATION_SIX);
    }
    
    public DieGraphic() {
        
        rotation = ROTATION_TWO;
        
        this.getMaterial().setDiffuse(new Vector4f(0, 0, 0, 1));
        this.getMaterial().setTextureModulator(1.0f);
        this.setPosition(new Vector3f(0, 0.7f, 0));
        this.setMesh(ModelManager.getModel("cube"));
    }
    
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
    
    public void setDieRoll(int dieRoll) {
        setRotation(DIE_ROLL_ROTATIONS.get(dieRoll - 1));
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
        
        this.rotate(rotation.x, rotation.y, rotation.z);
//        angle += dt;
//        rotate(angle, angle, angle);
    }
    
    @Override
    public void render(Shader shader) {
        TextureManager.getTexture("temp_tex").bind();
        super.render(shader);
        TextureManager.getTexture("temp_tex").unbind();
    }
    
}
