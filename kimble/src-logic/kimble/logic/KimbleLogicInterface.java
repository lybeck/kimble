package kimble.logic;

import java.util.List;
import java.util.Map;
import kimble.logic.board.Board;

/**
 *
 * @author Christoffer
 */
public interface KimbleLogicInterface {

    public Board getBoard();

    public List<Map<Integer, Integer>> getStartingDieRolls();

    public Team getStartingTeam();

    public List<Team> getTeams();

    public Team getTeam(int teamID);

    public void executeMove();

    public boolean isGameOver();

    public Team getNextTeamInTurn();
    
    public Turn getCurrentTurn();
    
    public IPlayer getCurrentPlayer();
    
    public boolean isAutoPlayer();

    public int getDieRoll();

    public String getMoveMessage();

    public Move getSelectedMove();

    public int getWinner();

    public List<Team> getFinishedTeams();

    public int getTurnCount();

    public boolean isFinished(int teamID);

    public boolean isDisqualified(int teamID);
}
