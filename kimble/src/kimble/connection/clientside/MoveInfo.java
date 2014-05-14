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

    public final Integer moveId;
    public final Integer pieceId;
    public final Boolean isHome;
    public final Integer startSquareId;
    public final Integer destSquareId;

    public MoveInfo(Integer moveId, Integer pieceId, Boolean isHome, Integer startSquareId, Integer destSquareId) {
        this.moveId = moveId;
        this.pieceId = pieceId;
        this.isHome = isHome;
        this.startSquareId = startSquareId;
        this.destSquareId = destSquareId;
    }

}
