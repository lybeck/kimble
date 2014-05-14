/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import kimble.graphic.Screen;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class ServerGame {

    private final KimbleLogic logic;
    private KimbleGraphic graphic;

    public static final boolean DEBUG = false;

    public final static int NUMBER_OF_TEAMS = 4;
    public final static int NUMBER_OF_PIECES = 4;
    public final static int SQUARES_FROM_START_TO_START = 8;
    
    private final boolean noGui;

    public ServerGame(boolean noGui, List<IPlayer> players) {
        this.noGui = noGui;
        this.logic = new KimbleLogic(players);
    }
    
    public void start() {
        if (noGui) {
            while (!logic.getGame().isGameOver()) {
                logic.executeMove();
            }
        } else {
            setupLWJGL();

            PlaybackProfile.setCurrentProfile(PlaybackProfile.FAST);

            graphic = new KimbleGraphic(logic);
            graphic.start();
        }
    }

    public ServerGame() {
        this.logic = null;
        this.noGui = true;
    }

    private void setupLWJGL() {
        Screen.setupNativesLWJGL();
        Screen.setupDisplay("Kimble - alpha 0.1", 800, 600);
        Screen.setupOpenGL();
        Screen.setResizable(true);
    }

    public KimbleLogic getLogic() {
        return logic;
    }

}
