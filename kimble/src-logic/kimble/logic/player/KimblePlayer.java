package kimble.logic.player;

import kimble.logic.IPlayer;
import kimble.logic.Move;
import kimble.logic.Piece;
import kimble.logic.Team;
import kimble.logic.Turn;

public class KimblePlayer implements IPlayer {

    private final String teamName;
    private Team team;
    private int selectedMove;

    public KimblePlayer(String teamName) {
        this.teamName = teamName;
        this.selectedMove = -2;
    }

    // TODO: Support for optional moves (selectedMove = -1)
    public boolean selectMove(Piece piece, int destinationID, Turn turn) {
        selectedMove = -2;
        for (int i = 0; i < turn.getMoves().size(); i++) {
            Move move = turn.getMoves().get(i);
            if (move.getPiece().equals(piece) && destinationID == move.getDestination().getID()) {
                selectedMove = i;
                return true;
            }
        }
        return false;
    }

    public int getSelectedMove() {
        return selectedMove;
    }

    @Override
    public boolean isAIPlayer() {
        return false;
    }

    @Override
    public void setMyTeam(Team team) {
        this.team = team;
        this.team.setName(teamName);
    }

    @Override
    public Team getMyTeam() {
        return team;
    }

    public final String getTeamName() {
        return team.getName();
    }
}
