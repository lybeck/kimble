package kimble.logic.player;

import java.util.List;
import kimble.logic.IPlayer;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.board.Board;

/**
 *
 * @author Lasse Lybeck
 */
public abstract class KimbleAI implements IPlayer {

    private final String teamName;
    private Team myTeam;
    private Board board;

    public KimbleAI(String teamName) {
        this.teamName = teamName;
    }

    public abstract int selectMove(Turn turn, List<Team> teams);

    @Override
    public boolean isAIPlayer() {
        return true;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setMyTeam(Team myTeam) {
        this.myTeam = myTeam;
        this.myTeam.setName(teamName);
    }

    public Team getMyTeam() {
        return myTeam;
    }

    public final String getTeamName() {
        return teamName;
    }
}
