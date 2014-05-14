package kimble.logic.player;

import kimble.logic.Game;
import kimble.logic.IPlayer;
import kimble.logic.Team;
import kimble.logic.Turn;

/**
 *
 * @author Lasse Lybeck
 */
public abstract class KimbleAI implements IPlayer {

    private final String teamName;
    private Team myTeam;

    public KimbleAI(String teamName) {
        this.teamName = teamName;
    }

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

    public final String getTeamName() {
        return teamName;
    }
}
