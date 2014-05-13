/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class ServerGame {

    private KimbleLogic logic;
    private KimbleGraphic graphic;

    public static final boolean DEBUG = true;

    public final static int NUMBER_OF_TEAMS = 4;
    public final static int NUMBER_OF_PIECES = 4;
    public final static int SQUARES_FROM_START_TO_START = 8;

    public ServerGame(boolean noGui, List<IPlayer> players) {

        this.logic = new KimbleLogic(players);

        if (noGui) {
            while (!logic.getGame().isGameOver()) {
                logic.executeMove();
            }
        } else {
//            PlaybackProfile.setCurrentProfile(PlaybackProfile.SLOW);
            PlaybackProfile.setCurrentProfile(PlaybackProfile.NORMAL);
//            PlaybackProfile.setCurrentProfile(PlaybackProfile.FAST);
//            PlaybackProfile.setCurrentProfile(PlaybackProfile.SUPER_FAST);

            graphic = new KimbleGraphic(logic);
            graphic.start();
        }

    }

}
