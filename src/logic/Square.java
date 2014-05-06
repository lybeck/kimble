package logic;

/**
 *
 * @author Lasse Lybeck
 */
public class Square {

    private Square next;
    private Square prev;
    private int teamSpawn;
    private Piece piece;

    public Square() {
        this.teamSpawn = -1;
    }

    public Square getNext() {
        return next;
    }

    public void setNext(Square next) {
        this.next = next;
    }

    public Square getPrev() {
        return prev;
    }

    public void setPrev(Square prev) {
        this.prev = prev;
    }

    public int getTeamSpawn() {
        return teamSpawn;
    }

    public void setTeamSpawn(int teamSpawn) {
        this.teamSpawn = teamSpawn;
    }

    public boolean isSpawnSquare() {
        return teamSpawn > -1;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isPiecePresent() {
        return this.piece != null;
    }
}
