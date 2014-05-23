/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger.entries;

/**
 *
 * @author Christoffer
 */
public class MoveEntry extends AbstractEntry {

    public final int pieceID;
    public final int startSquareID;
    public final int destSquareID;
    public final boolean home;
    public final boolean optional;

    public MoveEntry(int teamID, int dieRoll, int pieceID, Integer startSquareID, int destSquareID, Boolean home, Boolean optional) {
        super(teamID, dieRoll);

        this.pieceID = pieceID;
        if (startSquareID == null) {
            this.startSquareID = -1;
        } else {
            this.startSquareID = startSquareID;
        }
        this.destSquareID = destSquareID;
        if (home == null) {
            this.home = false;
        } else {
            this.home = home;
        }
        if (optional == null) {
            this.optional = false;
        } else {
            this.optional = optional;
        }
    }

}
