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

    public final Integer teamId;
    public final Integer pieceId;
    public final Boolean isHome;
    public final Integer squareId;

    public PieceInfo(Integer teamId, Integer pieceId, Boolean isHome, Integer squareId) {
        this.teamId = teamId;
        this.pieceId = pieceId;
        this.isHome = isHome;
        this.squareId = squareId;
    }

}
