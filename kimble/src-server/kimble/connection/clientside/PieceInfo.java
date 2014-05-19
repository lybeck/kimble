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
public class PieceInfo {

    private final int teamId;
    private final int pieceId;
    private final boolean home;
    private final Integer squareId;

    public PieceInfo(int teamId, int pieceId, boolean isHome, Integer squareId) {
        this.teamId = teamId;
        this.pieceId = pieceId;
        this.home = isHome;
        this.squareId = squareId;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getPieceId() {
        return pieceId;
    }

    public boolean isHome() {
        return home;
    }

    public Integer getSquareId() {
        return squareId;
    }
}
