package kimble.connection.messages;

import java.util.ArrayList;
import java.util.List;
import kimble.logic.Game;
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

    public MoveMessage(Turn turn, Game game) {
        data = new MessageData(turn, game);
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

        MessageData(Turn turn, Game game) {
            dieRoll = turn.getDieRoll();
            availableMoves = new ArrayList<>();
            for (Move move : turn.getMoves()) {
                availableMoves.add(new MoveInfo(move));
            }
            pieces = new ArrayList<>();
            for (Team team : game.getTeams()) {
                for (Piece piece : team.getPieces()) {
                    pieces.add(new PieceInfo(piece));
                }
            }
        }
    }

    private class PieceInfo {

        int teamId;
        int pieceId;
        Boolean isHome;
        Integer squareId;

        PieceInfo(Piece piece) {
            pieceId = piece.getId();
            teamId = piece.getTeamId();
            isHome = piece.getPosition() == null;
            if (!isHome) {
                isHome = null;
                squareId = piece.getPosition().getID();
            }
        }
    }

    private class MoveInfo {

        int pieceId;
        Boolean startHome;
        Integer startSquareId;
        int destSquareId;

        MoveInfo(Move move) {
            pieceId = move.getPiece().getId();
            startHome = move.getPiece().getPosition() == null;
            if (!startHome) {
                startHome = null;
                startSquareId = move.getPiece().getPosition().getID();
            }
            destSquareId = move.getDestination().getID();
        }
    }
}
