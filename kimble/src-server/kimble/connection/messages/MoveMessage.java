package kimble.connection.messages;

import java.util.ArrayList;
import java.util.List;
import kimble.logic.Move;
import kimble.logic.Piece;
import kimble.logic.Team;
import kimble.logic.Turn;

/**
 *
 * @author Lasse Lybeck
 */
public class MoveMessage extends SendMessage {

    private final MessageData data;

    public MoveMessage(Turn turn, List<Team> teams) {
        data = new MessageData(turn, teams);
    }

    @Override
    protected String getType() {
        return "moves";
    }

    @Override
    protected Object getData() {
        return data;
    }

    private class MessageData {

        int dieRoll;
        List<MoveInfo> availableMoves;
        List<PieceInfo> pieces;

        MessageData(Turn turn, List<Team> teams) {
            dieRoll = turn.getDieRoll();
            availableMoves = new ArrayList<>();
            for (int i = 0; i < turn.getMoves().size(); i++) {
                availableMoves.add(new MoveInfo(i, turn.getMoves().get(i)));
            }
            pieces = new ArrayList<>();
            for (Team team : teams) {
                for (Piece piece : team.getPieces()) {
                    pieces.add(new PieceInfo(piece));
                }
            }
        }
    }

    private class MoveInfo {

        int moveId;
        int pieceId;
        Boolean isHome;
        Boolean isOptional;
        Integer startSquareId;
        int destSquareId;

        MoveInfo(int moveId, Move move) {
            this.moveId = moveId;
            this.pieceId = move.getPiece().getId();
            this.isHome = move.getPiece().getPosition() == null;
            if (move.isOptional()) {
                this.isOptional = true;
            }
            if (!isHome) {
                this.isHome = null;
                this.startSquareId = move.getPiece().getPosition().getID();
            }
            this.destSquareId = move.getDestination().getID();
        }
    }

    private class PieceInfo {

        int teamId;
        int pieceId;
        Boolean isHome;
        Integer squareId;

        PieceInfo(Piece piece) {
            this.pieceId = piece.getId();
            this.teamId = piece.getTeamId();
            this.isHome = piece.getPosition() == null;
            if (!isHome) {
                this.isHome = null;
                this.squareId = piece.getPosition().getID();
            }
        }
    }

}
