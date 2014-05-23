package kimble.connection.clientside;

/**
 *
 * @author Lasse Lybeck
 */
public class SquareInfo {

    private final int squareId;
    private final boolean isStartSquare;
    private final boolean isGoalSquare;
    private final int teamId;

    public SquareInfo(int squareId) {
        this.squareId = squareId;
        this.isStartSquare = false;
        this.isGoalSquare = false;
        this.teamId = -1;
    }

    public SquareInfo(int squareId, boolean startSquare, boolean goalSquare, int teamId) {
        this.squareId = squareId;
        this.isStartSquare = startSquare;
        this.isGoalSquare = goalSquare;
        this.teamId = teamId;
    }

    public int getSquareId() {
        return squareId;
    }

    public boolean isStartSquare() {
        return isStartSquare;
    }

    public boolean isGoalSquare() {
        return isGoalSquare;
    }

    public int getTeamId() {
        return teamId;
    }
}
