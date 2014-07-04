package kimble.logic;

/**
 *
 * @author Lasse Lybeck
 */
public interface IPlayer {

    boolean isAIPlayer();
    
    void setMyTeam(Team myTeam);
    
    Team getMyTeam();
}
