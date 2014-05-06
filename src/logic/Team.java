package logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
public class Team {

    private final int id;
    private final List<Piece> pieces;

    public Team(int id, int numberOfPieces) {
        this.id = id;
        this.pieces = new ArrayList<>(numberOfPieces);
        for (int i = 0; i < numberOfPieces; i++) {
            pieces.add(new Piece(id));
        }
    }

    public int getId() {
        return id;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
    
    public Piece getPiece(int i) {
        return pieces.get(i);
    }
}
