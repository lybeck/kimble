package logic;

import logic.board.Square;
import logic.exception.IllegalMoveException;

/**
 *
 * @author Lasse Lybeck
 */
public class Move {

    private final Piece piece;
    private final Square destination;
    private final IllegalMoveException illegalMoveException;

    Move(Game game, Piece piece, int dieRoll) {
        this.piece = piece;
        IllegalMoveException exc = null;
        Square dest = null;
        if (piece.isHome()) {
            if (!game.getStartValues().contains(dieRoll)) {
                exc = new IllegalMoveException("Cannot start with a " + dieRoll + ".");
            } else {
                dest = game.getBoard().getStartSquare(piece.getTeamId());
            }
        } else {
            // TODO: move into goal squares
            dest = piece.getPosition();
            for (int i = 0; i < dieRoll; i++) {
                dest = dest.getNext();
            }
        }

        // trying to eat own?
        if (dest != null && dest.isPiecePresent() && dest.getPiece().getTeamId() == piece.getTeamId()) {
            exc = new IllegalMoveException("Cannot eat own piece!");
        }

        this.destination = dest;
        this.illegalMoveException = exc;
    }

    void execute() throws IllegalMoveException {
        if (illegalMoveException != null) {
            throw illegalMoveException;
        }
        if (destination.isPiecePresent()) {
            destination.getPiece().setPosition(null);
        }
        Square oldPosition = piece.getPosition();
        if (oldPosition != null) {
            oldPosition.setPiece(null);
        }
        piece.setPosition(destination);
        destination.setPiece(piece);
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getDestination() {
        return destination;
    }

    public boolean isValidMove() {
        return illegalMoveException == null;
    }
}
