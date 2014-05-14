/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.PlaybackProfile;
import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.utils.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DieHolderDomeGraphic extends Model {

    public DieHolderDomeGraphic() {
        this.getMaterial().setDiffuse(new Vector4f(0.7f, 0.7f, 1, 0.2f));
        this.getMaterial().setSpecular(new Vector4f(1, 1, 1, 0.2f));
        this.getMaterial().setAmbient(new Vector4f(0.2f, 0.2f, 0.2f, 0.2f));
        this.setPosition(new Vector3f(0, 0.4f, 0));
        this.setMesh(ModelManager.getModel("game_board_die_holder_dome"));
    }

    public void bounce() {
        angle = (float) Math.PI;
    }

    float angle = (float) Math.PI;

    @Override
    public void update(float dt) {
        super.update(dt);
        angle = MathHelper.lerp(angle, (float) (2 * Math.PI), dt * 20 * PlaybackProfile.currentProfile.getTurnTimeSpeedUp());
        setPosition(new Vector3f(0, (float) (0.1 * Math.sin(angle)), 0));
    }

}
