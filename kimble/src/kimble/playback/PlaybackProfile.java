/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.playback;

/**
 *
 * @author Christoffer
 */
public enum PlaybackProfile {

    // TURN_TIME_SPEEDUP = 1f is fine when TURN_TIME_STEP > 2f.
    // When the playing time increases the animations wont keep up if not adjusted with this.
    // DieGraphic, DieHolderGraphic and PieceGraphic uses this in their update method
    SLOW(5f, 0.5f),
    NORMAL(2f, 1f),
    MEDIUM(1f, 2f),
    FAST(0.5f, 4f),
    SUPER_FAST(0.1f, 4f),
    OUT_OF_CONTROL(0.01f, 4f);

    private final float turnTimeStep;
    private final float turnTimeSpeedUp;

    PlaybackProfile(float turnTimeStep, float turnTimeSpeedUp) {
        this.turnTimeStep = turnTimeStep;
        this.turnTimeSpeedUp = turnTimeSpeedUp;
    }

    public float getTurnTimeStep() {
        return turnTimeStep;
    }

    public float getTurnTimeSpeedUp() {
        return turnTimeSpeedUp;
    }

    public static PlaybackProfile currentProfile;

    public static void setCurrentProfile(PlaybackProfile profile) {
        currentProfile = profile;
    }

}
