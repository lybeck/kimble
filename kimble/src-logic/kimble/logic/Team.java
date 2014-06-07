package kimble.logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
public class Team {

    private final int id;
    private String name;
    private final List<Piece> pieces;

    private boolean finished;

    public Team(int id, int numberOfPieces) {
        this.id = id;
        this.pieces = new ArrayList<>(numberOfPieces);
        for (int i = 0; i < numberOfPieces; i++) {
            pieces.add(new Piece(i, id));
        }

        finished = false;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

}
