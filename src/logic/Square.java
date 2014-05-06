package logic;

/**
 *
 * @author Lasse Lybeck
 */
public class Square {

    private Square next;
    private Square prev;
    private Piece piece;

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
