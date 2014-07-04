package kimble.graphic.input;

import kimble.KimbleGraphic;
import kimble.playback.PlaybackProfile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public class ExtraInput extends Input {

    private final KimbleGraphic graphic;

    private boolean executeNextMove = false;
    private boolean executePreviousMove = false;

    public ExtraInput(KimbleGraphic graphic) {
        this.graphic = graphic;
    }

    @Override
    public void inputMouse(float dt) {
        // not used
    }

    @Override
    public void inputKeyboard(float dt) {

        while (Keyboard.next()) {
            // Update playback speed
            if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.SLOW);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.NORMAL);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.MEDIUM);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.FAST);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.SUPER_FAST);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
                PlaybackProfile.setCurrentProfile(PlaybackProfile.OUT_OF_CONTROL);
            } //
            /*
             * Extra input for debugging purposes.
             */ //
            else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
                graphic.setRotateCamera(!graphic.isRotateCamera());
                graphic.setUpdateCameraPosition(true);
                Mouse.setGrabbed(false);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
                graphic.setUpdateCameraPosition(!graphic.isUpdateCameraPosition());
                graphic.setRotateCamera(false);
                Mouse.setGrabbed(!graphic.isUpdateCameraPosition());
            } //
            /*
             * Manual Movement
             */ //
            else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                graphic.tryExecuteNextMove();
            }
        }
    }

}
