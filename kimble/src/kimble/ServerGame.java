package kimble;

import java.util.List;
import java.util.Random;
import kimble.logic.IPlayer;
import kimble.playback.PlaybackProfile;

/**
 *
 * @author Christoffer
 */
public class ServerGame {

    public static final boolean DEBUG = false;

    private final KimbleGameLogic logic;
    private KimbleGraphic graphic;

    private final boolean useGui;

    public ServerGame(List<IPlayer> players) {
        this(true, players);
    }

    public ServerGame(boolean useGui, List<IPlayer> players) {
        this.useGui = useGui;
        this.logic = new KimbleGameLogic(players, new Random().nextLong(), DEBUG);
    }

    public void start() {
        if (useGui) {
            graphic = new KimbleGraphic(logic, PlaybackProfile.FAST);
            graphic.start();
        } else {
            while (!logic.getGame().isGameOver()) {
                logic.executeMove();
            }
        }
    }

    public KimbleGameLogic getLogic() {
        return logic;
    }

}
