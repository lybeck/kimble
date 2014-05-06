package logic;

/**
 *
 * @author Lasse Lybeck
 */
public class Piece {

    private final int team;
    private Square position;

    public Piece(int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        this.position = position;
    }
}
