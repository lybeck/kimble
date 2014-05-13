package kimble.logic.player;

import kimble.logic.Game;
import kimble.logic.IPlayer;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.board.Board;

/**
 *
 * @author Lasse Lybeck
 */
public abstract class KimbleAI implements IPlayer {

    private Team myTeam;

    public abstract int selectMove(Turn turn, Game game);

    @Override
    public boolean isAIPlayer() {
        return true;
    }

    public void setMyTeam(Team myTeam) {
        this.myTeam = myTeam;
    }

    public Team getMyTeam() {
        return myTeam;
    }
}
