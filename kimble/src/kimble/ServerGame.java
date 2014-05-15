/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import kimble.playback.PlaybackProfile;
import java.util.List;
import kimble.graphic.Screen;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class ServerGame {

    public static final boolean DEBUG = false;

    private final KimbleGameLogic logic;
    private KimbleGraphic graphic;

    public final static int NUMBER_OF_TEAMS = 4;
    public final static int NUMBER_OF_PIECES = 4;
    public final static int SQUARES_FROM_START_TO_START = 8;

    private final boolean noGui;

    public ServerGame(boolean noGui, List<IPlayer> players) {
        this.noGui = noGui;
        this.logic = new KimbleGameLogic(players);
    }

    public void start() {
        if (noGui) {
            while (!logic.getGame().isGameOver()) {
                logic.executeMove();
            }
        } else {
            graphic = new KimbleGraphic(logic, PlaybackProfile.FAST);
            graphic.start();
        }
    }

    public ServerGame() {
        this.logic = null;
        this.noGui = true;
    }

    public KimbleGameLogic getLogic() {
        return logic;
    }

}
