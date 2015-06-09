package kimble.graphic.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.playback.PlaybackProfile;
import kimble.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class DieGraphic extends Model {

    public static final Vector3f ROTATION_ONE = new Vector3f(0, 0, (float) Math.toRadians(90));
    public static final Vector3f ROTATION_TWO = new Vector3f((float) Math.toRadians(90), 0, 0);
    public static final Vector3f ROTATION_THREE = new Vector3f((float) Math.toRadians(180), 0, 0);
    public static final Vector3f ROTATION_FOUR = new Vector3f(0, 0, 0);
    public static final Vector3f ROTATION_FIVE = new Vector3f((float) Math.toRadians(-90), 0, 0);
    public static final Vector3f ROTATION_SIX = new Vector3f(0, 0, (float) Math.toRadians(-90));

    public static final List<Vector3f> DIE_ROLL_ROTATIONS = new ArrayList<>();

    private static final String MODEL_KEY = "game_die";
    private static final String TEXTURE_KEY = "Die_tex";

    private static final float speedFactor = 5;

    static {
        DIE_ROLL_ROTATIONS.add(ROTATION_ONE);
        DIE_ROLL_ROTATIONS.add(ROTATION_TWO);
        DIE_ROLL_ROTATIONS.add(ROTATION_THREE);
        DIE_ROLL_ROTATIONS.add(ROTATION_FOUR);
        DIE_ROLL_ROTATIONS.add(ROTATION_FIVE);
        DIE_ROLL_ROTATIONS.add(ROTATION_SIX);
    }

    private final Random random = new Random();

    private Vector3f targetRotation;

    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;

    public DieGraphic() {

        targetRotation = DIE_ROLL_ROTATIONS.get(random.nextInt(DIE_ROLL_ROTATIONS.size()));

        this.getMaterial().setTextureModulator(1);
        this.setPosition(new Vector3f(0, 0.7f, 0));
        this.setMesh(ModelManager.getModel(MODEL_KEY));
    }

    public void setDieRoll(int dieRoll, float animationDuration) {
        if (dieRoll <= 0) {
            return;
        }
        this.targetRotation = DIE_ROLL_ROTATIONS.get(dieRoll - 1);

        int upperLimit = 5;
        int turnsX = upperLimit - random.nextInt(upperLimit + 1);
        int turnsY = upperLimit - random.nextInt(upperLimit + 1);
        int turnsZ = upperLimit - random.nextInt(upperLimit + 1);

        float x = (float) (targetRotation.x + turnsX * 2 * Math.PI);
        float y = (float) (targetRotation.y + turnsY * 2 * Math.PI);
        float z = (float) (targetRotation.z + turnsZ * 2 * Math.PI);

        this.targetRotation = new Vector3f(x, y, z);
        
        System.out.println("Current rotation:");
        System.out.println(this.getRotation().x);
        System.out.println(this.getRotation().y);
        System.out.println(this.getRotation().z);
        System.out.println("");
        System.out.println("Target rotation:");
        System.out.println(targetRotation.x);
        System.out.println(targetRotation.y);
        System.out.println(targetRotation.z);

//        int turnsX = 1;
//        int turnsY = 1;
//        int turnsZ = 1;
//
//        int rotationDirection = random.nextInt(3);
//        if (rotationDirection == 0) {
//            turnsX = random.nextInt(2) + 1;
//            turnsY = random.nextInt(2) + 1;
//        } else if (rotationDirection == 1) {
//            turnsX = random.nextInt(2) + 1;
//            turnsZ = random.nextInt(2) + 1;
//        } else if (rotationDirection == 2) {
//            turnsY = random.nextInt(2) + 1;
//            turnsZ = random.nextInt(2) + 1;
//        }
//
//        float x = (float) (targetRotation.x + turnsX * 2 * Math.PI);
//        float y = (float) (targetRotation.y + turnsY * 2 * Math.PI);
//        float z = (float) (targetRotation.z + turnsZ * 2 * Math.PI);
//
//        this.targetRotation = new Vector3f(x, y, z);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Math.abs(targetRotation.x - angleX) >= 0.01
                || Math.abs(targetRotation.y - angleY) >= 0.01
                || Math.abs(targetRotation.z - angleZ) >= 0.01) {

            angleX = MathHelper.lerp(angleX, targetRotation.x, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
                    * speedFactor);
            angleY = MathHelper.lerp(angleY, targetRotation.y, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
                    * speedFactor);
            angleZ = MathHelper.lerp(angleZ, targetRotation.z, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
                    * speedFactor);

            this.rotate(angleX, angleY, angleZ);
        }
    }

    @Override
    public void render(Shader shader, Camera camera) {
        TextureManager.getTexture(TEXTURE_KEY).bind();
        super.render(shader, camera);
        TextureManager.getTexture(TEXTURE_KEY).unbind();
    }

}
