package kimble.graphic;

import kimble.playback.PlaybackProfile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Christoffer
 */
public class ExtraInput {

    private boolean rotateCamera = false;

    private boolean updateCameraPosition = true;

    private boolean executeNextMove = false;
    private boolean executePreviousMove = false;

    public void update(float dt) {
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
            }
            
            // Random input
            else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
                rotateCamera = !rotateCamera;
                updateCameraPosition = !rotateCamera;
                Mouse.setGrabbed(false);
            } 
            
            // Manual movement
            else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                executeNextMove = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                executePreviousMove = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
                updateCameraPosition = !updateCameraPosition;
                Mouse.setGrabbed(!updateCameraPosition);
            }
        }
    }

    public void setExecuteNextMove(boolean executeMove) {
        this.executeNextMove = executeMove;
    }

    public boolean isExecuteNextMove() {
        return executeNextMove;
    }

    public void setExecutePreviousMove(boolean playbackPreviousMove) {
        this.executePreviousMove = playbackPreviousMove;
    }

    public boolean isExecutePreviousMove() {
        return executePreviousMove;
    }

    public boolean isRotateCamera() {
        return rotateCamera;
    }

    public boolean isUpdateCameraPosition() {
        return updateCameraPosition;
    }

}
