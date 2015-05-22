/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import java.util.Random;
import kimble.logic.IPlayer;
import kimble.playback.PlaybackProfile;

/**
 *
 * @author Christoffer Fridlund
 */
public class KimbleHotSeat {

    public static final boolean DEBUG = false;

    private final KimbleGameLogic logic;
    private KimbleGraphic graphic;

    public KimbleHotSeat(List<IPlayer> players) {
        this.logic = new KimbleGameLogic(players, new Random().nextLong(), DEBUG);
    }

    public void start() {
        graphic = new KimbleGraphic(logic, PlaybackProfile.FAST);
        graphic.start();
    }

    public KimbleGameLogic getLogic() {
        return logic;
    }
}
