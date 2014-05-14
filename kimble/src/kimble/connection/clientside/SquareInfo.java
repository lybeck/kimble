package kimble.connection.clientside;

/**
 *
 * @author Lasse Lybeck
 */
public class SquareInfo {

    private final int squareId;
    private final boolean startSquare;
    private final boolean goalSquare;
    private final int teamId;

    public SquareInfo(int squareId) {
        this.squareId = squareId;
        this.startSquare = false;
        this.goalSquare = false;
        this.teamId = -1;
    }

    public SquareInfo(int squareId, boolean startSquare, boolean goalSquare, int teamId) {
        this.squareId = squareId;
        this.startSquare = startSquare;
        this.goalSquare = goalSquare;
        this.teamId = teamId;
    }

    public int getSquareId() {
        return squareId;
    }

    public boolean isStartSquare() {
        return startSquare;
    }

    public boolean isGoalSquare() {
        return goalSquare;
    }

    public int getTeamId() {
        return teamId;
    }
}
