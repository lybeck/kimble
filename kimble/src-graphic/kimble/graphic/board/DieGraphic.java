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

    public static final List<Vector3f> DIE_ROLL_ROTATIONS = new ArrayList<>();

//    private static String MODEL_KEY = "cube";
//    private static String TEXTURE_KEY = "temp_tex";
    private static String MODEL_KEY = "game_die";
    private static String TEXTURE_KEY = "Die_tex";

    private static float speedFactor = 5;

    static {
        DIE_ROLL_ROTATIONS.add(ROTATION_ONE);
        DIE_ROLL_ROTATIONS.add(ROTATION_TWO);
        DIE_ROLL_ROTATIONS.add(ROTATION_THREE);
        DIE_ROLL_ROTATIONS.add(ROTATION_FOUR);
        DIE_ROLL_ROTATIONS.add(ROTATION_FIVE);
        DIE_ROLL_ROTATIONS.add(ROTATION_SIX);
    }

    private final Random random = new Random();

    private Vector3f rotation;

    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;

    public DieGraphic() {

        rotation = DIE_ROLL_ROTATIONS.get(random.nextInt(DIE_ROLL_ROTATIONS.size()));

//        this.getMaterial().setDiffuse(new Vector4f(0, 0, 0, 1));
        this.getMaterial().setLightPosition(new Vector4f(0, 10, 0, 1));
        this.getMaterial().setTextureModulator(0.75f);
        this.setPosition(new Vector3f(0, 0.7f, 0));
        this.setMesh(ModelManager.getModel(MODEL_KEY));
    }

    public void setDieRoll(int dieRoll) {
        if (dieRoll <= 0) {
            return;
        }
        this.rotation = DIE_ROLL_ROTATIONS.get(dieRoll - 1);

        int turnsX = 0;
        int turnsY = 0;
        int turnsZ = 0;

        int rotationDirection = random.nextInt(3);
        if (rotationDirection == 0) {
            turnsX = 2 * (random.nextInt(2) + 1);
            turnsY = 2 * (random.nextInt(2) + 1);
        } else if (rotationDirection == 1) {
            turnsX = 2 * (random.nextInt(2) + 1);
            turnsZ = 2 * (random.nextInt(2) + 1);
        } else if (rotationDirection == 2) {
            turnsY = 2 * (random.nextInt(2) + 1);
            turnsZ = 2 * (random.nextInt(2) + 1);
        }

        float x = (float) (rotation.x + turnsX * Math.PI);
        float y = (float) (rotation.y + turnsY * Math.PI);
        float z = (float) (rotation.z + turnsZ * Math.PI);

        this.rotation = new Vector3f(x, y, z);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Math.abs(rotation.x - angleX) >= 0.01
                || Math.abs(rotation.y - angleY) >= 0.01
                || Math.abs(rotation.z - angleZ) >= 0.01) {

            angleX = MathHelper.lerp(angleX, rotation.x, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
                    * speedFactor);
            angleY = MathHelper.lerp(angleY, rotation.y, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
                    * speedFactor);
            angleZ = MathHelper.lerp(angleZ, rotation.z, dt * PlaybackProfile.currentProfile.getTurnTimeSpeedUp()
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
