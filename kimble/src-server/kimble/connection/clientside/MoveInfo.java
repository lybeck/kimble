/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.clientside;

/**
 *
 * @author Christoffer
 */
public class MoveInfo {

    private final int moveId;
    private final int pieceId;
    private final boolean home;
    private final boolean optional;
    private final Integer startSquareId;
    private final int destinationSquareId;

    public MoveInfo(int moveId, int pieceId, boolean isHome, boolean isOptional, Integer startSquareId, int destSquareId) {
        this.moveId = moveId;
        this.pieceId = pieceId;
        this.home = isHome;
        this.optional = isOptional;
        this.startSquareId = startSquareId;
        this.destinationSquareId = destSquareId;
    }

    public int getMoveId() {
        return moveId;
    }

    public int getPieceId() {
        return pieceId;
    }

    public boolean isHome() {
        return home;
    }

    public boolean isOptional() {
        return optional;
    }

    public Integer getStartSquareId() {
        return startSquareId;
    }

    public int getDestinationSquareId() {
        return destinationSquareId;
    }
}
