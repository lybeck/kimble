package kimble.logic;

import kimble.logic.board.Square;
import kimble.logic.exception.IllegalMoveException;

/**
 *
 * @author Lasse Lybeck
 */
public class Move {

    private final Piece piece;
    private final Square destination;
    private final boolean optional;
    private final transient IllegalMoveException illegalMoveException;

    Move(Game game, Piece piece, int dieRoll) {
        this.piece = piece;
        if (piece.getPosition() != null) {
            this.optional = piece.getPosition().isGoalSquare();
        } else {
            this.optional = false;
        }
        IllegalMoveException exc = null;
        Square dest = null;
        if (piece.isHome()) {
            if (!game.getStartValues().contains(dieRoll)) {
                exc = new IllegalMoveException("Cannot start with a " + dieRoll + ".");
            } else {
                dest = game.getBoard().getStartSquare(piece.getTeamId());
            }
        } else {

            // this is used when in goal squares
            boolean movingBack = false;

            dest = piece.getPosition();
            Square next;
            for (int i = 0; i < dieRoll; i++) {
                if (movingBack) {
                    next = dest.getPrev();
                } else {
                    next = dest.getNext();
                }
                if (next == null) {
                    // at last goal square; start going back
                    movingBack = true;
                    next = dest.getPrev();
                }
                if (!movingBack && next.equals(game.getBoard().getStartSquare(piece.getTeamId()))) {
                    next = game.getBoard().getGoalSquare(piece.getTeamId(), 0);
                }
                dest = next;
            }
            if (movingBack && dest.isRegularSquare()) {
                // backed out from the goal squares
                exc = new IllegalMoveException("Cannot move out from the goal squares!");
            }
        }

        // trying to eat own?
        if (exc == null && dest != null && dest.isPiecePresent() && dest.getPiece().getTeamId() == piece.getTeamId()) {
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

    public boolean isOptional() {
        return optional;
    }

    public boolean isValidMove() {
        return illegalMoveException == null;
    }
}
