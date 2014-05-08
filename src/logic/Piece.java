package logic;

import logic.board.Square;

/**
 *
 * @author Lasse Lybeck
 */
public class Piece {

    private final int id;
    private final int teamId;
    private Square position;

    public Piece(int id, int teamId) {
        this.id = id;
        this.teamId = teamId;
    }

    public boolean isHome() {
        return this.position == null;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public Square getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }
}
