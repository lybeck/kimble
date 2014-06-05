package kimble;

import kimble.playback.PlaybackProfile;
import java.util.List;
import kimble.logic.Constants;
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
    public final static int NUMBER_OF_FINISHING_TEAMS = 1;
    public final static int SQUARES_FROM_START_TO_START = 8;

    private final boolean useGui;

    public ServerGame(List<IPlayer> players) {
        this(true, players);
    }

    public ServerGame(boolean useGui, List<IPlayer> players) {
        this.useGui = useGui;
        this.logic = new KimbleGameLogic(players, Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, NUMBER_OF_PIECES, SQUARES_FROM_START_TO_START, NUMBER_OF_FINISHING_TEAMS, DEBUG);
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
