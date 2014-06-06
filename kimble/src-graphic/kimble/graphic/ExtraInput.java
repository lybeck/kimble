/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private boolean playbackNextMove = false;
    private boolean playbackPreviousMove = false;

    public void update(float dt) {
        while (Keyboard.next()) {
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

            if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
                rotateCamera = true;
                Mouse.setGrabbed(false);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
                rotateCamera = false;
                Mouse.setGrabbed(true);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                executeMove = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                playbackNextMove = true;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                playbackPreviousMove = true;
            }
        }
    }

    private boolean executeMove = false;

    public void setExecuteMove(boolean executeMove) {
        this.executeMove = executeMove;
    }

    public boolean isExecuteMove() {
        return executeMove;
    }

    public void setPlaybackNextMove(boolean playbackNextMove) {
        this.playbackNextMove = playbackNextMove;
    }

    public boolean isPlaybackNextMove() {
        return playbackNextMove;
    }

    public void setPlaybackPreviousMove(boolean playbackPreviousMove) {
        this.playbackPreviousMove = playbackPreviousMove;
    }

    public boolean isPlaybackPreviousMove() {
        return playbackPreviousMove;
    }

    public boolean rotateCamera() {
        return rotateCamera;
    }
}
