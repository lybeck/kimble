/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        this.rotation = new Vector3f(rotation.x, (float) ((2 * random.nextInt(2)) * Math.PI), rotation.z);
    }

    public void setDieRoll(int dieRoll) {
        setRotation(DIE_ROLL_ROTATIONS.get(dieRoll - 1));

        float x = (float) (rotation.x + 2 * (random.nextInt(3) + 1) * Math.PI);
        float y = (float) (2 * (random.nextInt(3) + 1) * Math.PI);
        float z = (float) (rotation.z + 2 * (random.nextInt(3) + 1) * Math.PI);

        System.out.println("x=" + x + ", y=" + y + ", z=" + z);
        this.rotation = new Vector3f(x, y, z);
    }

    float angleX = 0;
    float angleY = 0;
    float angleZ = 0;

    Random random = new Random();

    private float lerp(float a, float b, float dt) {
        return a + dt * (b - a);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Math.abs(rotation.x - angleX) >= 0.01
                || Math.abs(rotation.y - angleY) >= 0.01
                || Math.abs(rotation.z - angleZ) >= 0.01) {

            angleX = lerp(angleX, rotation.x, dt * 5);
            angleY = lerp(angleY, rotation.y, dt * 5);
            angleZ = lerp(angleZ, rotation.z, dt * 5);

//            System.out.println("angleX = " + angleX + " / " + rotation.x);
//            System.out.println("angleY = " + angleY + " / " + rotation.y);
//            System.out.println("angleZ = " + angleZ + " / " + rotation.z);
//            time += dt;
            this.rotate(angleX, angleY, angleZ);
        } else {
            this.rotate(rotation.x, rotation.y, rotation.z);
        }
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
