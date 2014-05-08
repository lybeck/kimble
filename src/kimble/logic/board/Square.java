package kimble.logic.board;

import kimble.logic.Piece;

/**
 *
 * @author Lasse Lybeck
 */
public abstract class Square {

    private final int id;

    private Square next;
    private Square prev;
    private Piece piece;

    public Square(int id) {
        this.id = id;
    }

    public final int getID() {
        return id;
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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isPiecePresent() {
        return this.piece != null;
    }

    public abstract boolean isRegularSquare();

    public abstract boolean isGoalSquare();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Square)) {
            return false;
        }
        return ((Square) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        return hash;
    }
}
